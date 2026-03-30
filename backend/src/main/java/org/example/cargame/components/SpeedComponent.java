package org.example.cargame.components;

import org.example.cargame.snapshot.SpeedSnapshot;

public class SpeedComponent extends Component {
    private volatile SpeedSnapshot snapshot;

    public SpeedSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(SpeedSnapshot snapshot) {
        this.snapshot = snapshot;
    }
}
