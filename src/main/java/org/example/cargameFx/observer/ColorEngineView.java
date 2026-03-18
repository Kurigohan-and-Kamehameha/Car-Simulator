package org.example.cargameFx.observer;

import javafx.application.Platform;
import org.example.cargameFx.enums.EngineType;
import org.example.cargameFx.subject.Model;
import org.springframework.stereotype.Component;

@Component
public class ColorEngineView extends CarView implements EngineObserver, ColorObserver{

    private String color;
    private EngineType engineType;

    public ColorEngineView(Model model) {
        super(model);
        car.getEngineComponent().addObserver(this);
        car.getCol().addObserver(this);
        this.color = car.getCol().getColor();
        this.engineType = car.getEngineComponent().getActiveEngine().getType();
    }

    @Override
    public void update() {
        this.color = car.getCol().getColor();
        this.engineType = car.getEngineComponent().getActiveEngine().getType();
    }

    public EngineType getEngineType() {
        return this.engineType;
    }

    public String getColor() {
        return this.color;
    }

}
