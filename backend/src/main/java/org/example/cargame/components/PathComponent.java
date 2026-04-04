package org.example.cargame.components;

import org.example.cargame.snapshot.PathSnapshot;

public class PathComponent extends Component<PathSnapshot> {
    private volatile PathSnapshot snapshot;

    public PathSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(PathSnapshot snapshot) {
        this.snapshot = snapshot;
    }
}
