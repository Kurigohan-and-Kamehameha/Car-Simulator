package org.example.cargame.factories;

import org.example.cargame.components.EngineComponent;
import org.example.cargame.engine.Engine;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public class EngineFactory {
    private final Map<String, Engine> engines;

    public EngineFactory(Map<String, Engine> engines) {
        this.engines = engines;
    }

    public Collection<Engine> getEngines(){
        return engines.values();
    }

    public EngineComponent create() {
        return new EngineComponent(engines);
    }
}
