package org.example.cargame.controller;

import org.example.cargame.enums.EngineType;
import org.example.cargame.Servicelayer;

public class FXController {

    private final Servicelayer servicelayer;

    public FXController(Servicelayer servicelayer){
        this.servicelayer = servicelayer;
    }

    public void setColor(String color){
        servicelayer.setColor(color);
    }

    public void setEngine(EngineType engineType){
        servicelayer.setEngine(engineType);
    }

}
