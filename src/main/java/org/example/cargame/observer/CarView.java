package org.example.cargame.observer;

import org.example.cargame.entity.Car;
import org.example.cargame.subject.Model;

abstract class  CarView {
    protected final Car car;

    public CarView(Model model){
        this.car = (Car) model.getEntityList().getFirst();
    }
}
