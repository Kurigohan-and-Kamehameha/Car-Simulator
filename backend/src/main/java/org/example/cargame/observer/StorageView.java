package org.example.cargame.observer;

import org.example.cargame.CarModel;
import org.example.cargame.entity.EntityId;
import org.example.cargame.snapshot.EnergyStorageSnapshot;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StorageView extends ParentView<CarModel> implements StorageObserver {

    private final Map<EntityId, EnergyStorageSnapshot> cache = new ConcurrentHashMap<>();

    public StorageView(CarModel model, ObserverDispatcher dispatcher) {
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
        EnergyStorageSnapshot snap = model.getStorage().get(id).getSnapshot();
        cache.put(id, snap);
        model.getStorage().get(id).addObserver(this);
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
        model.getStorage().get(id).removeObserver(this);
    }

    @Override
    public void update(EntityId id) {
        cache.put(id, model.getStorage().get(id).getSnapshot());
        dispatcher.dispatch(() -> super.notifyObservers(id));
    }

    public double getPower(EntityId id) {
        EnergyStorageSnapshot snap = cache.get(id);
        return snap != null ? snap.getPercentage100() : 0;
    }
}
