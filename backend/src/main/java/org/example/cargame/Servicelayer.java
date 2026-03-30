package org.example.cargame;

import jakarta.annotation.PostConstruct;
import org.example.cargame.commands.SetDirectionCommand;
import org.example.cargame.engine.Engine;
import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.EngineType;
import org.example.cargame.enums.State;
import org.example.cargame.factories.CarFactory;
import org.example.cargame.factories.EngineFactory;
import org.example.cargame.graph.Graph;
import org.example.cargame.observer.*;
import org.example.cargame.persistence.*;
import org.example.cargame.snapshot.EnergyStorageSnapshot;
import org.example.cargame.snapshot.SpeedSnapshot;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Servicelayer {

    private final CarModel model;
    private final CommandQueue commands;
    private final ObserverDispatcher dispatcher;
    private final Dijkstra dij;
    private final Graph graph;
    private final PersistenceLayerJson persistenceLayerJson;
    private final CarFactory carFactory;
    private final EngineFactory engineFactory;
    private final PersistenceLayerDataBase persistenceLayerDataBase;
    private final GameLoader loader;
    private final List<ParentView<CarModel>> views;
    private final ColorView viewColor;
    private final EngineView viewEngine;
    private final MessageView viewMessage;
    private final PositionView viewPosition;
    private final StorageView viewStorage;
    private final SpeedView viewSpeed;
    private final StateView viewState;
    private final EntityManager<Model> entityManager;


    private EntityId playerId;

    public Servicelayer(CarModel model, CommandQueue commands, ObserverDispatcher dispatcher, Dijkstra dij, Graph graph,
                        PersistenceLayerJson persistenceLayerJson, CarFactory carFactory, EngineFactory engineFactory,
                        PersistenceLayerDataBase persistenceLayerDataBase, List<ParentView<CarModel>> views, ColorView viewColor,
                        EngineView viewEngine, MessageView viewMessage, PositionView viewPosition, StorageView viewStorage, SpeedView viewSpeed, StateView viewState,
                        EntityManager<Model> entityManager) {
        this.commands = commands;
        this.dispatcher = dispatcher;
        this.model = model;
        this.dij = dij;
        this.graph = graph;

        this.persistenceLayerJson = persistenceLayerJson;
        this.carFactory = carFactory;
        this.engineFactory = engineFactory;
        this.persistenceLayerDataBase = persistenceLayerDataBase;
        this.views = views;
        this.viewColor = viewColor;
        this.viewEngine = viewEngine;
        this.viewMessage = viewMessage;
        this.viewPosition = viewPosition;
        this.viewStorage = viewStorage;
        this.viewSpeed = viewSpeed;
        this.viewState = viewState;
        this.entityManager = entityManager;
        this.loader = new GameLoader();

        //playerId = carFactory.createCar(model, "I");
        //System.out.println("id#############" +carFactory.createCar2(model));
        //applyToView();
    }

    @PostConstruct
    public void init() {
        //playerId = new EntityId(0);
        //load(null);
        commands.submit(() -> {
                createEntity();
                removeEntity(playerId.getId());
                //playerId = carFactory.createCarLater(model, "I");
                //views.forEach(view -> view.bind(playerId));
                //EntityId id = carFactory.createCarLater(model, "D");
                //views.forEach(view -> view.bind(id));
                //applyToView();
        });
    }

    public void createEntity(){
        playerId = entityManager.createEntity(model);
        views.forEach(view -> view.bind(playerId));
    }

    public void removeEntity(int id){
        views.forEach(view -> view.unbind(new EntityId(id)));
        entityManager.removeEntity(model, playerId);
        System.out.println("All entites " +model.getAllEntities().size());
    }

    public void setDirection(String targetId) {
        System.out.println(Thread.currentThread().getName());
        commands.submit(new SetDirectionCommand(
                model, dij, graph, dispatcher, playerId, targetId));
    }

    public void setColor(String color) {
        commands.submit(() -> {
            if (State.WAIT_AT_WORKSHOP == model.getStates().get(playerId).get()) {
                model.getColors().get(playerId).setColor(color);

                dispatcher.dispatch(() -> {
                    model.getColors().get(playerId).notifyObservers(playerId);
                });
            }
        });
    }

    public void setSpeed(double speed) {
        commands.submit(() -> {
            model.getSpeeds().get(playerId).setSnapshot(new SpeedSnapshot(speed));
        });
    }

    public void setEngine(EngineType engineType) {
        commands.submit(() -> {
            if (State.WAIT_AT_WORKSHOP == model.getStates().get(playerId).get()) {
                model.getEngines().get(playerId).setEngine(engineType);
                Engine engine = model.getEngines().get(playerId).getActiveEngine();
                model.getStorage().get(playerId)
                        .setSnapshot(new EnergyStorageSnapshot(engine.capacity(), engine.capacity()));

                dispatcher.dispatch(() -> {
                    model.getEngines().get(playerId).notifyObservers(playerId);
                });
            }
        });
    }

    public double getPosX(int id) {
        System.out.println(Thread.currentThread().getName());
        return viewPosition.getPositionX(new EntityId(id));
    }

    public double getPosY(int id) {
        return viewPosition.getPositionY(new EntityId(id));
    }

    public String getColor(int id) {
        return viewColor.getColor(new EntityId(id));
    }

    public EngineType getEngine(int id) {
        return viewEngine.getEngineType(new EntityId(id));
    }

    public double getPower(int id) {
        return viewStorage.getPower(new EntityId(id));
    }

    public State getState(int id) {
        return viewState.getState(new EntityId(id));
    }

    public String getWarningMessage(int id) {
        return viewMessage.warning(new EntityId(id));
    }

    public String getAlertMessage(int id) {
        return viewMessage.alert(new EntityId(id));
    }

    public double getSpeed(int id) {
        return viewSpeed.getSpeed(new EntityId(id));
    }

    public Graph getGraph() {
        return this.graph;
    }

    public boolean isEntityBindToView(int id) {
        return model.isBind(new EntityId(id));
    }

    public List<Integer> getAllEntities() {
        return model.getAllEntities().stream()
                .map(EntityId::getId)
                .toList();
    }

    public void save(String path) {
        SnapshotBuilder snapshotBuilder = new SnapshotBuilder();
        LoadedGameData data = snapshotBuilder.build(model);
        persistenceLayerDataBase.save(path, data);
    }

    public void load(String path) {
        LoadedGameData  data = persistenceLayerDataBase.load(path);
        commands.submit(() -> {
            if(data == null) {
                return;
            }
            loader.apply(data, model, engineFactory);
            views.forEach(ParentView::rebind);
        });
    }

    private void applyToView() {
        dispatcher.dispatch(() -> {
            for (EntityId id : model.getAllEntities()) {
                model.getEngines().get(id).notifyObservers(id);
                model.getColors().get(id).notifyObservers(id);
                model.getSpeeds().get(id).notifyObservers(id);
                model.getMessages().get(id).notifyObservers(id);
                model.getStorage().get(id).notifyObservers(id);
                model.getPositions().get(id).notifyObservers(id);
                model.getStates().get(id).notifyObservers(id);
            }
        });
    }

}
