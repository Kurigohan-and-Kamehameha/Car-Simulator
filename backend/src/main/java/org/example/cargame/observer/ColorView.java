package org.example.cargame.observer;

import org.example.cargame.CarModel;
import org.example.cargame.entity.EntityId;
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
    }

    @Override
    public void update(EntityId id) {
        cache.put(id, model.getColors().get(id).getColor());
        super.notifyObservers(id);
    }

    public String getColor(EntityId id) {
        return cache.get(id) != null ? cache.get(id) : "#000000";
    }

}
