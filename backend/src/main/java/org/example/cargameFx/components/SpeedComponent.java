package org.example.cargameFx.components;

import org.example.cargameFx.snapshot.SpeedSnapshot;

public class SpeedComponent extends Component {
    private volatile SpeedSnapshot snapshot;

    public SpeedSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(SpeedSnapshot snapshot) {
        this.snapshot = snapshot;
    }
}
