package org.example.cargame.components;

import org.example.cargame.observer.ObserverDispatcher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ColorComponent extends org.example.cargame.components.Component {
    private volatile String color;

    public void setColor(String color){
        this.color = color;
    }

    public String getColor(){
        return this.color;
    }
}
