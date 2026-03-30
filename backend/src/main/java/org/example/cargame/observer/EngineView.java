package org.example.cargame.observer;

import org.example.cargame.CarModel;
import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.EngineType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EngineView extends ParentView<CarModel> implements EngineObserver {
    private final Map<EntityId, EngineType> engineTypes = new ConcurrentHashMap<>();

    public EngineView(CarModel model) {
        super(model);

        bind();
    }

    @Override
    public void bind() {
        for (EntityId id : model.getAllEntities()) {
            EngineType type = model.getEngines().get(id)
                    .getActiveEngine()
                    .getType();
            engineTypes.put(id, type);

            model.getEngines().get(id).addObserver(this);
        }
    }

    @Override
    public void bind(EntityId id) {
        EngineType type = model.getEngines().get(id)
                .getActiveEngine()
                .getType();
        engineTypes.put(id, type);
        model.getEngines().get(id).addObserver(this);
    }

    @Override
    public void rebind() {
        engineTypes.clear();
        bind();
    }

    @Override
    public void unbind(EntityId id) {
        engineTypes.remove(id);
        model.getEngines().get(id).removeObserver(this);
    }

    @Override
    public void update(EntityId id) {
        EngineType type = model.getEngines().get(id)
                .getActiveEngine()
                .getType();

        engineTypes.put(id, type);
    }

    public EngineType getEngineType(EntityId id) {
        return engineTypes.get(id);
    }

}
