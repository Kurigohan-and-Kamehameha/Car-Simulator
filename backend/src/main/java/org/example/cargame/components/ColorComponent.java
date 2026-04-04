package org.example.cargame.components;

import org.example.cargame.snapshot.ColorSnapshot;

public class ColorComponent extends Component<ColorSnapshot> {
    private volatile ColorSnapshot snapshot;

    public ColorSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(ColorSnapshot snapshot) {
        this.snapshot = snapshot;
    }
}
