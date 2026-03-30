package org.example.cargame.observer;

import org.example.cargame.CarModel;
import org.example.cargame.entity.EntityId;
import org.example.cargame.snapshot.EnergyStorageSnapshot;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StorageView extends ParentView<CarModel> implements StorageObserver {

    private final Map<EntityId, EnergyStorageSnapshot> snapshots = new ConcurrentHashMap<>();

    public StorageView(CarModel model) {
        super(model);

        bind();
    }

    @Override
    public void bind() {
        for (EntityId id : model.getAllEntities()) {
            EnergyStorageSnapshot snap = model.getStorage().get(id).getSnapshot();
            snapshots.put(id, snap);

            model.getStorage().get(id).addObserver(this);
        }
    }

    @Override
    public void bind(EntityId id) {
        EnergyStorageSnapshot snap = model.getStorage().get(id).getSnapshot();
        snapshots.put(id, snap);
        model.getStorage().get(id).addObserver(this);
    }

    @Override
    public void rebind() {
        snapshots.clear();
        bind();
    }

    @Override
    public void unbind(EntityId id) {
        snapshots.remove(id);
        model.getStorage().get(id).removeObserver(this);
    }

    @Override
    public void update(EntityId id) {
        snapshots.put(id, model.getStorage().get(id).getSnapshot());
    }

    public double getPower(EntityId id) {
        EnergyStorageSnapshot snap = snapshots.get(id);
        return snap.getPercentage100();
    }
}
