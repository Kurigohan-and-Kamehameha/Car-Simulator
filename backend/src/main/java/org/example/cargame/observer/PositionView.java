package org.example.cargame.observer;

import org.example.cargame.CarModel;
import org.example.cargame.entity.EntityId;
import org.example.cargame.snapshot.PositionSnapshot;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PositionView extends ParentView<CarModel> implements PositionObserver {
    private final Map<EntityId, PositionSnapshot> cache = new ConcurrentHashMap<>();

    public PositionView(CarModel model, ObserverDispatcher dispatcher) {
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
        PositionSnapshot snap = model.getPositions().get(id).getSnapshot();
        cache.put(id, snap);
        model.getPositions().get(id).addObserver(this);
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
        model.getPositions().get(id).removeObserver(this);
    }

    @Override
    public void update(EntityId id) {
        cache.put(id, model.getPositions().get(id).getSnapshot());
        dispatcher.dispatch(() -> super.notifyObservers(id));
    }

    public PositionSnapshot getPosition(EntityId id) {
        return cache.get(id);
    }

}