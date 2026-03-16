package org.example.cargame.config;

import org.example.cargame.components.*;
import org.example.cargame.enums.EngineType;
import org.example.cargame.enums.State;
import org.example.cargame.entity.Car;
import org.example.cargame.subject.Model;
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
