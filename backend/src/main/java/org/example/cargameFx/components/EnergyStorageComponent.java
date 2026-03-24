package org.example.cargameFx.components;

import org.example.cargameFx.snapshot.EnergyStorageSnapshot;

public class EnergyStorageComponent extends Component{
    private volatile EnergyStorageSnapshot snapshot;

    public EnergyStorageSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(EnergyStorageSnapshot snapshot) {
        this.snapshot = snapshot;
    }
}
