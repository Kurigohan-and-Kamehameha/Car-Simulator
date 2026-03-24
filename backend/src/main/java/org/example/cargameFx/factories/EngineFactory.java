package org.example.cargameFx.factories;

import org.example.cargameFx.components.EngineComponent;
import org.example.cargameFx.engine.Engine;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EngineFactory {
    private final Map<String, Engine> engines;

    public EngineFactory(Map<String, Engine> engines) {
        this.engines = engines;
    }

    public EngineComponent create() {
        return new EngineComponent(engines);
    }
}
