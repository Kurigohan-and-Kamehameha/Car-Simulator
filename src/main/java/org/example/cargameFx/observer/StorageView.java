package org.example.cargameFx.observer;

import org.example.cargameFx.Model;
import org.example.cargameFx.snapshot.EnergyStorageSnapshot;
import org.springframework.stereotype.Component;

@Component
public class StorageView extends CarView implements StorageObserver{
    private EnergyStorageSnapshot currentSnapshot;

    public StorageView(Model model) {
        super(model);
        currentSnapshot = model.getStorage().get(playerId).getSnapshot();
        model.getStorage().get(playerId).addObserver(this);
    }

    @Override
    public void update() {
        currentSnapshot = model.getStorage().get(playerId).getSnapshot();
    }

    public double getPower() {
        return currentSnapshot.getPercentage100();
    }
}
