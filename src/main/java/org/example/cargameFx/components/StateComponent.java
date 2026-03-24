package org.example.cargameFx.components;

import org.example.cargameFx.enums.State;

public class StateComponent extends Component{
    private volatile State state;

    public State get() {
        return state;
    }

    public void set(State newState) {
        this.state = newState;
    }
}
