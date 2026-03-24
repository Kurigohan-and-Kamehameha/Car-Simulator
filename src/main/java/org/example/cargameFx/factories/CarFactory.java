package org.example.cargameFx.factories;

import org.example.cargameFx.Model;
import org.example.cargameFx.components.*;
import org.example.cargameFx.entity.EntityId;
import org.example.cargameFx.enums.EngineType;
import org.example.cargameFx.enums.NodeType;
import org.example.cargameFx.enums.State;
import org.example.cargameFx.graph.Graph;
import org.example.cargameFx.graph.Node;
import org.example.cargameFx.snapshot.EnergyStorageSnapshot;
import org.example.cargameFx.snapshot.PositionSnapshot;
import org.example.cargameFx.snapshot.PositionSnapshotDriveFree;
import org.example.cargameFx.snapshot.VelocitySnapshot;
import org.springframework.stereotype.Service;

@Service
public class CarFactory {
    private final EngineFactory engineFactory;
    private final Graph graph;

    public CarFactory(EngineFactory engineFactory, Graph graph) {
        this.engineFactory = engineFactory;
        this.graph = graph;
    }

    public void createCar(Model model) {
        Node startNode = graph.getNodes().stream()
                .filter(node -> node.getType() == NodeType.WORKSHOP)
                .findFirst()
                .orElse(null);

        EntityId carId = model.createEntity();

        PositionComponent pos = new PositionComponent();
        pos.setSnapshot(new PositionSnapshot(startNode));
        //PositionComponentDriveFree pos = new PositionComponentDriveFree();
        //pos.setSnapshot(new PositionSnapshotDriveFree(50,50));

        VelocityComponent vel = new VelocityComponent();
        vel.setSnapshot(new VelocitySnapshot(2, 2));

        SpeedComponent speed = new SpeedComponent(80);

        ColorComponent color = new ColorComponent();
        color.setColor("#0000FF");

        EngineComponent engine = engineFactory.create();
        engine.setEngine(EngineType.FUEL);

        EnergyStorageComponent storage = new EnergyStorageComponent();
        storage.setSnapshot(new EnergyStorageSnapshot(engine.getActiveEngine().capacity(), engine.getActiveEngine().capacity()));

        StateComponent state = new StateComponent();
        state.set(State.WAIT_AT_WORKSHOP);

        PathComponent path = new PathComponent();

        model.setPlayer(carId);
        model.getPositions().put(carId, pos);
        //model.getPositionsDF().put(carId, pos);
        model.getVelocities().put(carId, vel);
        model.getStorage().put(carId, storage);
        model.getSpeeds().put(carId, speed);
        model.getColors().put(carId, color);
        model.getEngines().put(carId, engine);
        model.getStates().put(carId, state);
        model.getPaths().put(carId, path);
    }
}
