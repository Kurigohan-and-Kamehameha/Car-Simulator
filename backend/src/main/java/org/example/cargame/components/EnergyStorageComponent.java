package org.example.cargame.components;

import org.example.cargame.enums.EngineType;
import org.example.cargame.snapshot.EnergyStorage;
import org.example.cargame.snapshot.EnergyStorageSnapshot;

import java.util.HashMap;
import java.util.Map;

public class EnergyStorageComponent extends Component<EnergyStorageSnapshot> {
    private volatile EnergyStorageSnapshot snapshot;

    public EnergyStorageSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(EnergyStorageSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public void consume(EngineType engineType, double distance) {
        EnergyStorageSnapshot snapshot = this.snapshot;

        double newPower = Math.max(0, snapshot.get(engineType).power() - distance);

        Map<EngineType, EnergyStorage> energyStorageMap = new HashMap<>(snapshot.storageList());
        energyStorageMap.put(engineType, new EnergyStorage(newPower, snapshot.get(engineType).capacity()));
        setSnapshot(new EnergyStorageSnapshot(energyStorageMap));
    }

    public void refill(EngineType engineType) {
        EnergyStorageSnapshot snapshot = this.snapshot;

        Map<EngineType, EnergyStorage> energyStorageMap = new HashMap<>(snapshot.storageList());
        energyStorageMap.put(engineType, new EnergyStorage(snapshot.get(engineType).capacity(), snapshot.get(engineType).capacity()));
        setSnapshot(new EnergyStorageSnapshot(energyStorageMap));
    }
}
