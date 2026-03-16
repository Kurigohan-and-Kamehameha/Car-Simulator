package org.example.cargameFx.observer;

import org.example.cargameFx.subject.Model;

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