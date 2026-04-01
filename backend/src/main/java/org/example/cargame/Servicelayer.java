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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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

    private volatile boolean updateInProgress = true;
    private volatile boolean loadingCompleted = false;
    private EntityId playerId;

    public Servicelayer(CarModel model, CommandQueue commands, Dijkstra dij, Graph graph,
                        EngineFactory engineFactory, PersistenceLayerDataBase persistenceLayerDataBase,
                        List<ParentView<CarModel>> views, GameStateView gameStateView, EntityManager<Model> entityManager) {
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

    public void createEntity(String nodeId){
        CompletableFuture<EntityId> future = new CompletableFuture<>();
        commands.submit(() -> {
            try {
                updateInProgress = true;
                playerId = entityManager.createEntity(model, nodeId);
                views.forEach(view -> view.bind(playerId));
                future.complete(playerId);
            } catch (Exception e) {
                future.completeExceptionally(e);
            } finally {
                updateInProgress = false;
            }
        });
        try {
            playerId = future.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to create entity", e);
        }
    }

    public void removeEntity(int id){
        CompletableFuture<Void> future = new CompletableFuture<>();
        commands.submit(() -> {
            try {
                updateInProgress = true;
                gameStateView.remove(playerId);
                views.forEach(view -> view.unbind(playerId));
                entityManager.removeEntity(model, playerId);
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            } finally {
                updateInProgress = false;
            }
        });
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to remove entity", e);
        }
    }

    public void setDirection(String targetId) {
        commands.submit(new SetDirectionCommand(
                model, dij, graph, playerId, targetId));
    }

    public void setColor(String color) {
        commands.submit(() -> {
            if (State.WAIT_AT_WORKSHOP == model.getStates().get(playerId).get()) {
                model.getColors().get(playerId).setColor(color);
                model.getColors().get(playerId).notifyObservers(playerId);
            }
        });
    }

    public void setSpeed(double speed) {
        commands.submit(() -> model.getSpeeds().get(playerId).setSnapshot(new SpeedSnapshot(speed)));
    }

    public void setEngine(EngineType engineType) {
        commands.submit(() -> {
            if (State.WAIT_AT_WORKSHOP == model.getStates().get(playerId).get()) {
                model.getEngines().get(playerId).setEngine(engineType);
                Engine engine = model.getEngines().get(playerId).getActiveEngine();
                model.getStorage().get(playerId)
                        .setSnapshot(new EnergyStorageSnapshot(engine.capacity(), engine.capacity()));

                model.getEngines().get(playerId).notifyObservers(playerId);
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

    public boolean getLoadingCompete(){
        return loadingCompleted;
    }

    public boolean getUpdateInProgress(){
        return updateInProgress;
    }

    public void save() {
        SnapshotBuilder snapshotBuilder = new SnapshotBuilder();
        LoadedGameData data = snapshotBuilder.build(model);
        persistenceLayerDataBase.save(data);
    }

    public void load() {
        LoadedGameData  data = persistenceLayerDataBase.load();
        commands.submit(() -> {
            if(data == null) {
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
                        Map.Entry::getValue
                ));
    }
}
