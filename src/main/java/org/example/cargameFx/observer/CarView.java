package org.example.cargameFx.observer;

import org.example.cargameFx.entity.Car;
import org.example.cargameFx.subject.Model;

abstract class  CarView {
    protected final Car car;

    public CarView(Model model){
        this.car = (Car) model.getEntityList().getFirst();
    }
}
