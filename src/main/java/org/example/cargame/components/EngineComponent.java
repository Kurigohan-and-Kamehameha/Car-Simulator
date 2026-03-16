package org.example.cargame.components;

import org.example.cargame.enums.EngineType;
import org.example.cargame.engine.Engine;
import org.example.cargame.observer.ObserverDispatcher;
import org.springframework.cglib.proxy.Dispatcher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Scope("prototype")
public class EngineComponent extends org.example.cargame.components.Component {
    private final Map<String, Engine> engines;
    private Engine activeEngine;

    public EngineComponent(Map<String, Engine> engines) {
        this.engines = engines;
    }

    public void setEngine(EngineType type) {
        Engine engine = engines.get(type.name());

        if (engine == null) {
            throw new IllegalArgumentException("Unknown engine type: " + type);
        }

        this.activeEngine = engine;
    }

    public Engine getActiveEngine() {
        return activeEngine;
    }
}
