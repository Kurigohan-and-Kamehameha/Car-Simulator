package org.example.cargameFx.controller;

import org.example.cargameFx.enums.EngineType;
import org.example.cargameFx.Servicelayer;
import org.springframework.stereotype.Controller;

@Controller
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
