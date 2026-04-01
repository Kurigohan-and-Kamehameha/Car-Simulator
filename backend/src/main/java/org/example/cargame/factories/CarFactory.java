package org.example.cargame.factories;

import org.example.cargame.CarModel;
import org.example.cargame.components.*;
import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.EngineType;
import org.example.cargame.enums.MessageType;
import org.example.cargame.enums.NodeType;
import org.example.cargame.enums.State;
import org.example.cargame.graph.Graph;
import org.example.cargame.graph.Node;
import org.example.cargame.snapshot.EnergyStorageSnapshot;
import org.example.cargame.snapshot.PositionSnapshot;
import org.example.cargame.snapshot.SpeedSnapshot;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CarFactory {
    private final EngineFactory engineFactory;
    private final Graph graph;

    public CarFactory(EngineFactory engineFactory, Graph graph) {
        this.engineFactory = engineFactory;
        this.graph = graph;
    }

    public EntityId createCar(CarModel model, String nodeId) {
        Node startNode = graph.getNodes().stream()
                .filter(node -> node.getId().equals(nodeId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Node not found: " + nodeId));

        PositionComponent pos = new PositionComponent();
        pos.setSnapshot(new PositionSnapshot(startNode));

        SpeedComponent speed = new SpeedComponent();
        speed.setSnapshot(new SpeedSnapshot(80));

        ColorComponent color = new ColorComponent();
        color.setColor("#0000FF");

        EngineComponent engine = engineFactory.create();
        engine.setEngine(EngineType.FUEL);

        EnergyStorageComponent storage = new EnergyStorageComponent();
        storage.setSnapshot(
                new EnergyStorageSnapshot(engine.getActiveEngine().capacity(), engine.getActiveEngine().capacity()));

        StateComponent state = new StateComponent();

        MessageComponent msg = new MessageComponent();
        msg.setMessage(MessageType.ALERT, "");
        switch (Objects.requireNonNull(startNode).getType()) {
            case NodeType.WORKSHOP:
                state.set(State.WAIT_AT_WORKSHOP);
                msg.setMessage(MessageType.WARNING, "");
                break;
            case NodeType.INTERSECTION:
                state.set(State.WAIT_AT_INTERSECTION);
                msg.setMessage(MessageType.WARNING, "Must be at workshop to change engine or color.");
                break;
            case NodeType.GASSTATION:
                state.set(State.WAIT_AT_GASSTATION);
                msg.setMessage(MessageType.WARNING, "Must be at workshop to change engine or color.");
                break;
        }

        PathComponent path = new PathComponent();

        EntityId carId = model.createEntity();
        model.getPositions().put(carId, pos);
        model.getStorage().put(carId, storage);
        model.getSpeeds().put(carId, speed);
        model.getColors().put(carId, color);
        model.getEngines().put(carId, engine);
        model.getStates().put(carId, state);
        model.getPaths().put(carId, path);
        model.getMessages().put(carId, msg);

        return carId;
    }
}
