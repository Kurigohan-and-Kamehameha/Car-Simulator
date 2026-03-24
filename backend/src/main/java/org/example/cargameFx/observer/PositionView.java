package org.example.cargameFx.observer;

import org.example.cargameFx.snapshot.PositionSnapshot;
import org.example.cargameFx.Model;
import org.springframework.stereotype.Component;

@Component
public class PositionView extends PlayerView implements PositionObserver {
    private PositionSnapshot currentSnapshot;

    public PositionView(Model model) {
        super(model);
        currentSnapshot = model.getPositions().get(playerId).getSnapshot();
        model.getPositions().get(playerId).addObserver(this);
    }

    @Override
    public void update() {
        currentSnapshot = model.getPositions().get(playerId).getSnapshot();
    }

    public double getPositionX() {
        return currentSnapshot.x();
    }

    public double getPositionY() {
        return currentSnapshot.y();
    }
}