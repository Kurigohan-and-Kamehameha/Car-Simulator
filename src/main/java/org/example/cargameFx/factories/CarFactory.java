package org.example.cargameFx.factories;

import org.example.cargameFx.Model;
import org.example.cargameFx.components.*;
import org.example.cargameFx.entity.EntityId;
import org.example.cargameFx.enums.EngineType;
import org.example.cargameFx.enums.State;
import org.example.cargameFx.snapshot.PositionSnapshot;
import org.example.cargameFx.snapshot.VelocitySnapshot;
import org.springframework.stereotype.Service;

@Service
public class CarFactory {
    private final EngineFactory engineFactory;

    public CarFactory(EngineFactory engineFactory) {
        this.engineFactory = engineFactory;
    }

    public void createCar(Model model) {
        EntityId carId = model.createEntity();

        PositionComponent pos = new PositionComponent();
        pos.setSnapshot(new PositionSnapshot(150, 150));

        VelocityComponent vel = new VelocityComponent();
        vel.setSnapshot(new VelocitySnapshot(2, 2));

        SpeedComponent speed = new SpeedComponent(2);

        ColorComponent color = new ColorComponent();
        color.setColor("#00FF00");

        EngineComponent engine = engineFactory.create();
        engine.setEngine(EngineType.FUEL);

        StateComponent state = new StateComponent();
        state.state = State.WAIT_AT_WORKSHOP;

        model.setPlayer(carId);
        model.getPositions().put(carId, pos);
        model.getVelocities().put(carId, vel);
        model.getSpeeds().put(carId, speed);
        model.getColors().put(carId, color);
        model.getEngines().put(carId, engine);
        model.getStates().put(carId, state);
    }
}
