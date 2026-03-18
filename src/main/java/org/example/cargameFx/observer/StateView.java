package org.example.cargameFx.observer;

import org.example.cargameFx.Model;
import org.example.cargameFx.enums.State;
import org.springframework.stereotype.Component;

@Component
public class StateView extends CarView implements Observer{
    private volatile State state;

    public StateView(Model model) {
        super(model);
        this.state = model.getStates().get(playerId).state;
        model.getStates().get(playerId).addObserver(this);
    }

    @Override
    public void update() {
        this.state = model.getStates().get(playerId).state;
    }

    public State getState(){
        return this.state;
    }
}
