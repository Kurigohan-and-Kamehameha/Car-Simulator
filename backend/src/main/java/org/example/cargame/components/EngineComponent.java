package org.example.cargame.components;

import org.example.cargame.enums.EngineType;
import org.example.cargame.engine.Engine;
import org.example.cargame.snapshot.EngineSnapshot;

import java.util.Map;

public class EngineComponent extends Component<EngineSnapshot> {

    private final Map<String, Engine> engines;
    private volatile EngineSnapshot snapshot;

    public EngineSnapshot getSnapshot() {
        return snapshot;
    }

    public EngineComponent(Map<String, Engine> engines) {
        this.engines = engines;
    }

    public void setEngine(EngineType type) {
        Engine engine = engines.get(type.name());
        if (engine == null) {
            throw new IllegalArgumentException("Unknown engine type: " + type);
        }
        this.snapshot = new EngineSnapshot(engine);
    }
}
