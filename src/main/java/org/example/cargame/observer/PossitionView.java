package org.example.cargame.observer;

import org.example.cargame.entity.Car;
import org.example.cargame.subject.Model;

public class PossitionView extends CarView implements PositionObserver{

    public PossitionView(Model model) {
        super(model);
        car.getPos().addObserver(this);
    }

    @Override
    public void update() {
        synchronized (car.getLock()){
            System.out.println(car.getPos().getX() +" " +car.getPos().getY());
        }

    }
}