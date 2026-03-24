package org.example.cargameFx.components;

import org.example.cargameFx.snapshot.PositionSnapshot;

public class PositionComponent extends Component {
    private volatile PositionSnapshot snapshot;

    public PositionSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(PositionSnapshot snapshot) {
        this.snapshot = snapshot;
    }
}
