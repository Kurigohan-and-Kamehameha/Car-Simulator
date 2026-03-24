package org.example.cargameFx.observer;

import org.example.cargameFx.Model;
import org.example.cargameFx.enums.EngineType;
import org.springframework.stereotype.Component;

@Component
public class EngineView extends PlayerView implements EngineObserver {
    private volatile EngineType engineType;

    public EngineView(Model model) {
        super(model);
        this.engineType = model.getEngines().get(playerId).getActiveEngine().getType();
        model.getEngines().get(playerId).addObserver(this);
    }

    @Override
    public void update() {
        this.engineType = model.getEngines().get(playerId).getActiveEngine().getType();
    }

    public EngineType getEngineType() {
        return this.engineType;
    }

}
