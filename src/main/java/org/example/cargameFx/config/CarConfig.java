package org.example.cargameFx.config;

import org.example.cargameFx.components.*;
import org.example.cargameFx.enums.EngineType;
import org.example.cargameFx.enums.State;
import org.example.cargameFx.entity.Car;
import org.example.cargameFx.subject.Model;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class CarConfig {

    @Bean
    @Scope("prototype")
    public Car createCar(PositionComponent pos, VelocityComponent vel, ColorComponent col, EngineComponent engine, StateComponent state){
        Car car = new Car(pos, vel, col, engine, state);
        car.getCol().setColor("red");
        car.getEngineComponent().setEngine(EngineType.FUEL);
        car.getStateComponent().state = State.WAIT_AT_WORKSHOP;
        car.getPos().setX(20);
        car.getPos().setY(20);
        car.getVel().setVx(2);
        car.getVel().setVy(2);
        return car;
    }

    @Bean
    public Model model(Car car){
        Model model = new Model();
        model.addEntity(car);
        return model;
    }
}
