package org.example.cargame.components;


import org.example.cargame.snapshot.StateSnapshot;

public class StateComponent extends Component<StateSnapshot> {

    private volatile StateSnapshot snapshot;

    public StateSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(StateSnapshot snapshot) {
        this.snapshot = snapshot;
    }

}
