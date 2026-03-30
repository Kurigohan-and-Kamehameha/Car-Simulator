package org.example.cargame.components;

import org.example.cargame.snapshot.PositionSnapshot;

public class PositionComponent extends Component {
    private volatile PositionSnapshot snapshot;

    public PositionSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(PositionSnapshot snapshot) {
        this.snapshot = snapshot;
    }
}
