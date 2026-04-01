package org.example.cargame.observer;

import org.example.cargame.CarModel;
import org.example.cargame.entity.EntityId;
import org.example.cargame.snapshot.SpeedSnapshot;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SpeedView extends ParentView<CarModel> implements SpeedObserver {
    private final Map<EntityId, SpeedSnapshot> cache = new ConcurrentHashMap<>();

    public SpeedView(CarModel model, ObserverDispatcher dispatcher) {
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
        SpeedSnapshot snap = model.getSpeeds().get(id).getSnapshot();
        cache.put(id, snap);
        model.getSpeeds().get(id).addObserver(this);
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
        model.getSpeeds().get(id).removeObserver(this);
    }

    @Override
    public void update(EntityId id) {
        cache.put(id, model.getSpeeds().get(id).getSnapshot());
        dispatcher.dispatch(() -> super.notifyObservers(id));
    }

    public double getSpeed(EntityId id) {
        SpeedSnapshot snap = cache.get(id);
        return snap != null ? snap.speed() : 0;
    }
}
