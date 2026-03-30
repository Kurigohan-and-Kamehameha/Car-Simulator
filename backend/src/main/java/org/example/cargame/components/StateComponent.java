package org.example.cargame.components;

import org.example.cargame.enums.State;

public class StateComponent extends Component{
    private volatile State state;

    public State get() {
        return state;
    }

    public void set(State newState) {
        this.state = newState;
    }
}
