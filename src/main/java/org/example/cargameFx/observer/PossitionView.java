package org.example.cargameFx.observer;

import org.example.cargameFx.enums.EngineType;
import org.example.cargameFx.subject.Model;
import org.springframework.stereotype.Component;

@Component
public class PossitionView extends CarView implements PositionObserver{

    private double x, y;

    public PossitionView(Model model) {
        super(model);
        car.getPos().addObserver(this);
        x = car.getPos().getX();
        y = car.getPos().getY();
    }

    @Override
    public void update() {
        synchronized (car.getLock()){
            this.x = car.getPos().getX();
            this.y = car.getPos().getY();
        }
    }

    public double getPositionX() {
        return this.x;
    }

    public double getPositionY() {
        return this.y;
    }
}