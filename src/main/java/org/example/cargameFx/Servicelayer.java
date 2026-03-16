package org.example.cargameFx;

import org.example.cargameFx.components.VelocityComponent;
import org.example.cargameFx.entity.Car;
import org.example.cargameFx.enums.Direction;
import org.example.cargameFx.enums.EngineType;
import org.example.cargameFx.enums.State;
import org.example.cargameFx.observer.ObserverDispatcher;
import org.example.cargameFx.subject.Model;
import org.springframework.stereotype.Service;

@Service
public class Servicelayer {

    private final Car car;
    private final CommandQueue commands;
    private final ObserverDispatcher dispatcher;

    public Servicelayer(Model model, CommandQueue commands, ObserverDispatcher dispatcher) {
        this.commands = commands;
        this.dispatcher = dispatcher;
        this.car = (Car) model.getEntityList().getFirst();
    }

    public void setDirection(Direction type){
        commands.submit(() -> {
            synchronized (car.getLock()){
                if(State.WAIT_AT_INTERSECTION == car.getStateComponent().state
                        || State.WAIT_AT_WORKSHOP == car.getStateComponent().state) {
                    VelocityComponent vel = car.getVel();
                    vel.setVx(vel.getVx() * type.getDx());
                    vel.setVy(vel.getVy() * type.getDy());
                    car.getStateComponent().state = State.DRIVE;
                }
            }
        });
    }

    public void setColor(String color){
        commands.submit(() -> {
            synchronized (car.getLock()) {
                if (State.WAIT_AT_WORKSHOP == car.getStateComponent().state) {
                    car.getCol().setColor(color);
                    dispatcher.dispatch(() -> {
                        car.getCol().notifyObservers();
                    });
                }
            }

        });
    }

    public void setEngine(EngineType engineType){
        commands.submit(() -> {
            synchronized (car.getLock()){
                if(State.WAIT_AT_WORKSHOP == car.getStateComponent().state) {
                    car.getEngineComponent().setEngine(engineType);
                    dispatcher.dispatch(() -> {
                        car.getEngineComponent().notifyObservers();
                    });
                }
            }

        });
    }

}
