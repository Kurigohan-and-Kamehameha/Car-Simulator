package org.example.cargame.components;

import org.example.cargame.snapshot.EnergyStorageSnapshot;

public class EnergyStorageComponent extends Component<EnergyStorageSnapshot> {
    private volatile EnergyStorageSnapshot snapshot;

    public EnergyStorageSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(EnergyStorageSnapshot snapshot) {
        this.snapshot = snapshot;
    }

}
