package org.example.cargame.observer;

import org.example.cargame.CarModel;
import org.example.cargame.entity.EntityId;
import org.example.cargame.snapshot.EnergyStorageSnapshot;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StorageView extends ParentView<CarModel> implements PushObserver<EnergyStorageSnapshot> {

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
        model.getStorage().get(id).addObserver(this);
        dispatcher.dispatch(() -> cache.put(id, snap));
    }

    @Override
    public void rebind() {
        dispatcher.dispatch(cache::clear);
        bind();
    }

    @Override
    public void unbind(EntityId id) {
        model.getStorage().get(id).removeObserver(this);
        dispatcher.dispatch(() -> cache.remove(id));
    }

    @Override
    public void update(EntityId id, EnergyStorageSnapshot data) {
        cache.put(id, data);
    }

    public double getPower(EntityId id) {
        EnergyStorageSnapshot snap = cache.get(id);
        return snap != null ? snap.getPercentage100() : 0;
    }


}
