package org.example.cargame;

import org.example.cargame.commands.SetDirectionCommand;
import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.EngineType;
import org.example.cargame.enums.State;
import org.example.cargame.factories.EngineFactory;
import org.example.cargame.graph.Graph;
import org.example.cargame.observer.*;
import org.example.cargame.persistence.*;
import org.example.cargame.snapshot.ColorSnapshot;
import org.example.cargame.snapshot.EnergyStorageSnapshot;
import org.example.cargame.snapshot.GameStateDTO;
import org.example.cargame.snapshot.SpeedSnapshot;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class Servicelayer {
    private final CarModel model;
    private final CommandQueue commands;
    private final Dijkstra dij;
    private final Graph graph;
    private final EngineFactory engineFactory;
    private final PersistenceLayerDataBase persistenceLayerDataBase;
    private final GameLoader loader;
    private final List<ParentView<CarModel>> views;
    private final GameStateView gameStateView;
    private final EntityManager<Model> entityManager;
    private final ObserverDispatcher dispatcher;

    private volatile EntityId playerId;

    private final AtomicInteger pendingUpdates = new AtomicInteger(0);
    private volatile boolean loadingCompleted = true;

    public Servicelayer(CarModel model, CommandQueue commands, Dijkstra dij, Graph graph,
            EngineFactory engineFactory, PersistenceLayerDataBase persistenceLayerDataBase,
            List<ParentView<CarModel>> views, GameStateView gameStateView, EntityManager<Model> entityManager,
            ObserverDispatcher dispatcher) {
        this.commands = commands;
        this.model = model;
        this.dij = dij;
        this.graph = graph;
        this.engineFactory = engineFactory;
        this.persistenceLayerDataBase = persistenceLayerDataBase;
        this.views = views;
        this.gameStateView = gameStateView;
        this.entityManager = entityManager;
        this.loader = new GameLoader();
        this.dispatcher = dispatcher;
        this.playerId = model.getAllEntities()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public void createEntity(String nodeId) {
        pendingUpdates.incrementAndGet();
        commands.submit(() -> {
            playerId = entityManager.createEntity(model, nodeId);
            views.forEach(view -> view.bind(playerId));
            gameStateView.update(playerId);
            dispatcher.dispatch(() -> gameStateView.update(playerId));
            pendingUpdates.decrementAndGet();
        });

    }

    public void removeEntity(int id) {
        pendingUpdates.incrementAndGet();
        commands.submit(() -> {
            EntityId entityId = new EntityId(id);
            views.forEach(view -> view.unbind(entityId));
            entityManager.removeEntity(model, entityId);
            dispatcher.dispatch(() -> gameStateView.remove(entityId));
            pendingUpdates.decrementAndGet();
        });
    }

    public void setDirection(String targetId) {
        commands.submit(new SetDirectionCommand(
                model, dij, graph, playerId, targetId, dispatcher));
    }

    public void setColor(String color) {
        commands.submit(() -> {
            if (State.WAIT_AT_WORKSHOP == model.getStates().get(playerId).getSnapshot().state()) {
                var comp = model.getColors().get(playerId);
                comp.setSnapshot(new ColorSnapshot(color));

                dispatcher.dispatch(() -> comp.notifyObservers(playerId, comp.getSnapshot()));
            }
        });
    }

    public void setSpeed(double speed) {
        commands.submit(() -> {
            var comp = model.getSpeeds().get(playerId);
            comp.setSnapshot(new SpeedSnapshot(speed));

            dispatcher.dispatch(() -> comp.notifyObservers(playerId, comp.getSnapshot()));
        });
    }

    public void setEngine(EngineType engineType) {
        commands.submit(() -> {
            if (State.WAIT_AT_WORKSHOP == model.getStates().get(playerId).getSnapshot().state()) {
                var comp = model.getEngines().get(playerId);
                comp.setEngine(engineType);
                model.getStorage().get(playerId)
                        .setSnapshot(new EnergyStorageSnapshot(comp.getSnapshot().activeEngine().capacity(),
                                comp.getSnapshot().activeEngine().capacity()));
                dispatcher.dispatch(() -> comp.notifyObservers(playerId, comp.getSnapshot()));
            }
        });
    }

    public Graph getGraph() {
        return this.graph;
    }

    public List<Integer> getAllEntities() {
        return model.getAllEntities().stream()
                .map(EntityId::getId)
                .toList();
    }

    public boolean getLoadingCompete() {
        return loadingCompleted;
    }

    public boolean getUpdateInProgress() {
        return pendingUpdates.get() > 0;
    }

    public void save() {
        commands.submit(() -> {
            SnapshotBuilder snapshotBuilder = new SnapshotBuilder();
            LoadedGameData data = snapshotBuilder.build(model);
            CompletableFuture.runAsync(() -> persistenceLayerDataBase.save(data));
        });
    }

    public void load() {
        if (!loadingCompleted)
            return;
        loadingCompleted = false;

        LoadedGameData data = persistenceLayerDataBase.load();

        commands.submit(() -> {
            if (data == null) {
                loadingCompleted = true;
                return;
            }
            loader.apply(data, model, engineFactory);
            dispatcher.dispatch(gameStateView::clear);
            views.forEach(ParentView::rebind);
            dispatcher.dispatch(() -> loadingCompleted = true);
        });
    }

    public Map<Integer, GameStateDTO> getAllGameStates() {
        return gameStateView.getAll().entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().getId(),
                        Map.Entry::getValue));
    }
}
