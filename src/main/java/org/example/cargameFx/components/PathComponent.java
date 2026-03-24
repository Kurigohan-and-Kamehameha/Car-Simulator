package org.example.cargameFx.components;

import org.example.cargameFx.snapshot.PathSnapshot;

public class PathComponent extends Component {
    private volatile PathSnapshot snapshot;

    public PathSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(PathSnapshot snapshot) {
        this.snapshot = snapshot;
    }
}

