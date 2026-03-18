package org.example.cargameFx.factories;

import org.example.cargameFx.components.*;
import org.example.cargameFx.entity.Car;
import org.example.cargameFx.enums.EngineType;
import org.example.cargameFx.enums.State;
import org.springframework.stereotype.Service;

@Service
public class CarFactory {

    private final EngineFactory engineFactory;

    public CarFactory(EngineFactory engineFactory) {
        this.engineFactory = engineFactory;
    }

    public Car createCar() {
        Car car = new Car(new PositionComponent(), new VelocityComponent(), new SpeedComponent(2), new ColorComponent(), engineFactory.create(), new StateComponent());
        car.getCol().setColor("#00FF00");
        car.getEngineComponent().setEngine(EngineType.FUEL);
        car.getStateComponent().state = State.WAIT_AT_WORKSHOP;
        car.getPos().setX(150);
        car.getPos().setY(150);
        car.getVel().setVx(2);
        car.getVel().setVy(2);
        return car;
    }
}
