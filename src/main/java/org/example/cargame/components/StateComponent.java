package org.example.cargame.components;

import org.example.cargame.enums.State;
import org.springframework.stereotype.Component;

@Component
public class StateComponent extends org.example.cargame.components.Component{
    public volatile State state;
}
