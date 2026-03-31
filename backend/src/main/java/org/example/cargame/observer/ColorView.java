package org.example.cargame.observer;

import org.example.cargame.CarModel;
import org.example.cargame.components.ColorComponent;
import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.ActionType;
import org.example.cargame.events.EntityUpdateEvent;
import org.example.cargame.snapshot.EnergyStorageSnapshot;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ColorView extends ParentView<CarModel> implements ColorObserver {
    private final Map<EntityId, String> cache = new ConcurrentHashMap<>();

    public ColorView(CarModel model, ObserverDispatcher dispatcher) {
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
        String color = model.getColors().get(id).getColor();
        cache.put(id, color);
        model.getColors().get(id).addObserver(this);
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
        model.getColors().get(id).removeObserver(this);
        dispatcher.dispatch(() -> GameStateRegistry.notify(id, ActionType.REMOVE));
    }

    @Override
    public void update(EntityId id) {
        cache.put(id, model.getColors().get(id).getColor());
        dispatcher.dispatch(() -> GameStateRegistry.notify(id, ActionType.UPDATE));
    }

    public String getColor(EntityId id) {
        return cache.get(id) != null ? cache.get(id) : "#000000";
    }

}
