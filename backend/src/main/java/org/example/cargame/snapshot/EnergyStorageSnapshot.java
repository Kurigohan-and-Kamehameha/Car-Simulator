package org.example.cargame.snapshot;

import org.example.cargame.enums.EngineType;

import java.util.Map;

public record EnergyStorageSnapshot(Map<EngineType, EnergyStorage> storageList) {
    public EnergyStorageSnapshot(Map<EngineType, EnergyStorage> storageList) {
        this.storageList = Map.copyOf(storageList);
    }

    public Map<EngineType, EnergyStorage> get() {
        return this.storageList;
    }

    public EnergyStorage get(EngineType engineType) {
        return this.storageList.get(engineType);
    }
}
