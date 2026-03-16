package org.example.cargame.observer;

import org.example.cargame.entity.Car;
import org.example.cargame.subject.Model;

public class ColorEngineView extends CarView implements EngineObserver, ColorObserver{

    public ColorEngineView(Model model) {
        super(model);
        car.getEngineComponent().addObserver(this);
        car.getCol().addObserver(this);
    }

    @Override
    public void update() {
        System.out.println(car.getCol().getColor());
        System.out.println(car.getEngineComponent().getActiveEngine().getType());
        System.out.println(Thread.currentThread().getName());
    }
}
