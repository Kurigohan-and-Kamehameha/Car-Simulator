package org.example.cargameFx.observer;

import org.example.cargameFx.Model;
import org.springframework.stereotype.Component;

@Component
public class ColorView extends PlayerView implements ColorObserver {
    private volatile String color;

    public ColorView(Model model) {
        super(model);
        this.color = model.getColors().get(playerId).getColor();
        model.getColors().get(playerId).addObserver(this);
    }

    @Override
    public void update() {
        this.color = model.getColors().get(playerId).getColor();
    }

    public String getColor() {
        return this.color;
    }

}
