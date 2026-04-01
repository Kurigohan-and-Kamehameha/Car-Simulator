package org.example.cargame.observer;

import org.example.cargame.CarModel;
import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.EngineType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EngineView extends ParentView<CarModel> implements EngineObserver {
    private final Map<EntityId, EngineType> cache = new ConcurrentHashMap<>();

    public EngineView(CarModel model, ObserverDispatcher dispatcher) {
        super(model, dispatcher);

        bind();
    }

    @Override
    public void bind() {
        for (EntityId id : model.getAllEntities()) {
            bind(id);
        }
    }

    @Override
    public void bind(EntityId id) {
        EngineType type = model.getEngines().get(id)
                .getActiveEngine()
                .getType();
        cache.put(id, type);
        model.getEngines().get(id).addObserver(this);
        dispatcher.dispatch(() -> super.notifyObservers(id));
    }

    @Override
    public void rebind() {
        cache.clear();
        bind();
    }

    @Override
    public void unbind(EntityId id) {
        cache.remove(id);
        model.getEngines().get(id).removeObserver(this);
        //dispatcher.dispatch(() -> super.notifyObservers(id));
    }

    @Override
    public void update(EntityId id) {
        cache.put(id, model.getEngines().get(id).getActiveEngine().getType());
        dispatcher.dispatch(() -> super.notifyObservers(id));
    }

    public EngineType getEngineType(EntityId id) {
        return cache.get(id);
    }

}
