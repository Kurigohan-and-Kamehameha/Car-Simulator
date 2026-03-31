package org.example.cargame.observer;

import org.example.cargame.CarModel;
import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.ActionType;
import org.example.cargame.enums.EngineType;
import org.example.cargame.events.EntityUpdateEvent;
import org.example.cargame.snapshot.EnergyStorageSnapshot;
import org.springframework.context.ApplicationEventPublisher;
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
        dispatcher.dispatch(() -> GameStateRegistry.notify(id, ActionType.UPDATE));
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
        dispatcher.dispatch(() -> GameStateRegistry.notify(id, ActionType.REMOVE));
    }

    @Override
    public void update(EntityId id) {
        cache.put(id, model.getEngines().get(id).getActiveEngine().getType());
        dispatcher.dispatch(() -> GameStateRegistry.notify(id, ActionType.UPDATE));
    }

    public EngineType getEngineType(EntityId id) {
        return cache.get(id);
    }

}
