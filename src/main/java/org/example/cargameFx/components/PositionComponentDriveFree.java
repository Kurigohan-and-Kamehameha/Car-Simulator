package org.example.cargameFx.components;

import org.example.cargameFx.snapshot.PositionSnapshotDriveFree;

public class PositionComponentDriveFree extends Component {
    private volatile PositionSnapshotDriveFree snapshot;

    public PositionSnapshotDriveFree getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(PositionSnapshotDriveFree snapshot) {
        this.snapshot = snapshot;
    }
}

