package org.example.cargameFx.observer;

import org.example.cargameFx.Model;
import org.example.cargameFx.snapshot.SpeedSnapshot;
import org.springframework.stereotype.Component;

@Component
public class SpeedView extends PlayerView implements SpeedObserver {
    private volatile SpeedSnapshot snapshot;

    public SpeedView(Model model) {
        super(model);
        this.snapshot = model.getSpeeds().get(playerId).getSnapshot();
        model.getSpeeds().get(playerId).addObserver(this);
    }

    @Override
    public void update() {
        this.snapshot = model.getSpeeds().get(playerId).getSnapshot();
    }

    public double getSpeed() {
        return this.snapshot.getSpeed();
    }
}
