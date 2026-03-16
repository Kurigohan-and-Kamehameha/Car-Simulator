package org.example.cargame;

import org.example.cargame.entity.Car;
import org.example.cargame.enums.State;
import org.example.cargame.subject.Model;

public class PhysicsEngine {

    private final Car car;

    public PhysicsEngine(Model model){
        this.car = (Car) model.getEntityList().getFirst();
    }

    public void update(){
        synchronized(car.getLock()) {
            if(State.DRIVE == car.getStateComponent().state) {
                double x = car.getPos().getX();
                double y = car.getPos().getY();

                double newx = x + car.getVel().getVx();
                double newy = y + car.getVel().getVy();

                car.getPos().setX(newx);
                car.getPos().setY(newy);
            }
        }
    }

    public void notifyObservers(){
        if(State.DRIVE == car.getStateComponent().state) {
            car.getPos().notifyObservers();
        }
    }
}
