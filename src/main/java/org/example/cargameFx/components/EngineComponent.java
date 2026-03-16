package org.example.cargameFx.components;

import org.example.cargameFx.enums.EngineType;
import org.example.cargameFx.engine.Engine;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Scope("prototype")
public class EngineComponent extends org.example.cargameFx.components.Component {
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
