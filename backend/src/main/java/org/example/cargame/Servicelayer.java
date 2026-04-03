package org.example.cargame;

import jakarta.annotation.PostConstruct;
import org.example.cargame.commands.SetDirectionCommand;
import org.example.cargame.engine.Engine;
import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.EngineType;
import org.example.cargame.enums.State;
import org.example.cargame.factories.EngineFactory;
import org.example.cargame.graph.Graph;
import org.example.cargame.observer.*;
import org.example.cargame.persistence.*;
import org.example.cargame.snapshot.EnergyStorageSnapshot;
import org.example.cargame.snapshot.GameStateDTO;
import org.example.cargame.snapshot.SpeedSnapshot;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

    private volatile boolean updateInProgress = true;
    private volatile boolean loadingCompleted = false;

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

    @PostConstruct
    public void init() {
        updateInProgress = false;
        loadingCompleted = true;
    }

    public void createEntity(String nodeId) {
        commands.submit(() -> {
            updateInProgress = true;
            playerId = entityManager.createEntity(model, nodeId);
            views.forEach(view -> view.bind(playerId));
            gameStateView.update(playerId);
            dispatcher.dispatch(() -> gameStateView.update(playerId));
            updateInProgress = false;
        });

    }

    public void removeEntity(int id) {
        commands.submit(() -> {
            updateInProgress = true;
            EntityId entityId = new EntityId(id);
            views.forEach(view -> view.unbind(entityId));
            entityManager.removeEntity(model, entityId);
            dispatcher.dispatch(() -> gameStateView.remove(entityId));
            updateInProgress = false;
        });
    }

    public void setDirection(String targetId) {
        commands.submit(new SetDirectionCommand(
                model, dij, graph, playerId, targetId, dispatcher));
    }

    public void setColor(String color) {
        commands.submit(() -> {
            if (State.WAIT_AT_WORKSHOP == model.getStates().get(playerId).get()) {
                model.getColors().get(playerId).setColor(color);
                dispatcher.dispatch(() -> model.getColors().get(playerId).notifyObservers(playerId));
            }
        });
    }

    public void setSpeed(double speed) {
        commands.submit(() -> {
            model.getSpeeds().get(playerId).setSnapshot(new SpeedSnapshot(speed));
            model.getSpeeds().get(playerId).notifyObservers(playerId);
        });
    }

    public void setEngine(EngineType engineType) {
        commands.submit(() -> {
            if (State.WAIT_AT_WORKSHOP == model.getStates().get(playerId).get()) {
                model.getEngines().get(playerId).setEngine(engineType);
                Engine engine = model.getEngines().get(playerId).getActiveEngine();
                model.getStorage().get(playerId)
                        .setSnapshot(new EnergyStorageSnapshot(engine.capacity(), engine.capacity()));

                dispatcher.dispatch(() -> model.getEngines().get(playerId).notifyObservers(playerId));
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
        return updateInProgress;
    }

    public void save() {
        commands.submit(() -> {
            SnapshotBuilder snapshotBuilder = new SnapshotBuilder();
            LoadedGameData data = snapshotBuilder.build(model);
            persistenceLayerDataBase.save(data);
        });
    }

    public void load() {
        LoadedGameData data = persistenceLayerDataBase.load();
        commands.submit(() -> {
            if (data == null) {
                return;
            }
            loadingCompleted = false;
            loader.apply(data, model, engineFactory);
            gameStateView.clear();
            views.forEach(ParentView::rebind);
            loadingCompleted = true;
        });
    }

    public Map<Integer, GameStateDTO> getAllGameStates() {
        return gameStateView.getAll().entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().getId(),
                        Map.Entry::getValue));
    }
}
