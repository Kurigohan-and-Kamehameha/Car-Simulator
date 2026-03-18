package org.example.cargameFx;

import org.example.cargameFx.components.VelocityComponent;
import org.example.cargameFx.entity.Car;
import org.example.cargameFx.enums.EngineType;
import org.example.cargameFx.enums.State;
import org.example.cargameFx.observer.ObserverDispatcher;
import org.example.cargameFx.subject.Model;
import org.springframework.stereotype.Service;

@Service
public class Servicelayer {

    private final Model model;
    private final CommandQueue commands;
    private final ObserverDispatcher dispatcher;

    public Servicelayer(Model model, CommandQueue commands, ObserverDispatcher dispatcher) {
        this.commands = commands;
        this.dispatcher = dispatcher;
        this.model = model;
    }

    public void stop(){
        Car car = (Car) model.getEntityList().getFirst();
        commands.submit(() -> {
            synchronized (car.getLock()){
                VelocityComponent vel = car.getVel();
                vel.setVx(0);
                vel.setVy(0);

                car.getStateComponent().state = State.WAIT_AT_INTERSECTION;
            }
        });
    }

    public void setDirection(int dx, int dy){
        Car car = (Car) model.getEntityList().getFirst();
        commands.submit(() -> {
            synchronized (car.getLock()){
                VelocityComponent vel = car.getVel();
                double speed = car.getSpeedComponent().getSpeed();
                double vx = vel.getVx();
                double vy = vel.getVy();
                vel.setVx(dx * speed);
                vel.setVy(dy * speed);

                car.getStateComponent().state = State.DRIVE;
            }
        });
    }

    public void setColor(String color, Runnable afterUpdate){
        Car car = (Car) model.getEntityList().getFirst();
        commands.submit(() -> {
            synchronized (car.getLock()) {
                if (State.WAIT_AT_WORKSHOP == car.getStateComponent().state) {
                    car.getCol().setColor(color);
                    dispatcher.dispatch(() -> {
                        car.getCol().notifyObservers();
                        if(afterUpdate != null){
                            afterUpdate.run();
                        }
                    });
                }
            }
        });
    }

    public void setEngine(EngineType engineType, Runnable afterUpdate){
        Car car = (Car) model.getEntityList().getFirst();
        commands.submit(() -> {
            synchronized (car.getLock()){
                if(State.WAIT_AT_WORKSHOP == car.getStateComponent().state) {
                    car.getEngineComponent().setEngine(engineType);
                    dispatcher.dispatch(() -> {
                        car.getEngineComponent().notifyObservers();
                        if(afterUpdate != null){
                            afterUpdate.run();
                        }
                    });
                }
            }
        });
    }

}
