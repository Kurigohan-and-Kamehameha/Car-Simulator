package org.example.cargameFx.components;

import org.example.cargameFx.snapshot.VelocitySnapshot;

public class VelocityComponent extends Component {
    private volatile VelocitySnapshot snapshot = new VelocitySnapshot(0,0);

    public VelocitySnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(VelocitySnapshot snapshot) {
        this.snapshot = snapshot;
    }
}
