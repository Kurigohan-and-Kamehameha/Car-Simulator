package org.example.cargameFx.observer;

import org.example.cargameFx.enums.EngineType;
import org.example.cargameFx.Model;
import org.springframework.stereotype.Component;

@Component
public class ColorEngineView extends CarView implements EngineObserver, ColorObserver{

    private volatile String color;
    private volatile EngineType engineType;

    public ColorEngineView(Model model) {
        super(model);
        this.color = model.getColors().get(playerId).getColor();
        this.engineType = model.getEngines().get(playerId).getActiveEngine().getType();
        model.getEngines().get(playerId).addObserver(this);
        model.getColors().get(playerId).addObserver(this);
    }

    @Override
    public void update() {
        this.color = model.getColors().get(playerId).getColor();
        this.engineType = model.getEngines().get(playerId).getActiveEngine().getType();
    }

    public EngineType getEngineType() {
        return this.engineType;
    }

    public String getColor() {
        return this.color;
    }

}
