package org.example.cargameFx.components;

import org.example.cargameFx.enums.State;
import org.springframework.stereotype.Component;

@Component
public class StateComponent extends org.example.cargameFx.components.Component{
    public volatile State state;
}
