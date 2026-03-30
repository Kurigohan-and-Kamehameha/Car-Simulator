package org.example.cargame.observer;

import org.example.cargame.CarModel;
import org.example.cargame.entity.EntityId;
import org.example.cargame.snapshot.SpeedSnapshot;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SpeedView extends ParentView<CarModel> implements SpeedObserver {
    private final Map<EntityId, SpeedSnapshot> snapshots = new ConcurrentHashMap<>();

    public SpeedView(CarModel model) {
        super(model);

        bind();
    }

    @Override
    public void bind() {
        for (EntityId id : model.getAllEntities()) {
            SpeedSnapshot snap = model.getSpeeds().get(id).getSnapshot();
            snapshots.put(id, snap);

            model.getSpeeds().get(id).addObserver(this);
        }
    }

    @Override
    public void bind(EntityId id) {
        SpeedSnapshot snap = model.getSpeeds().get(id).getSnapshot();
        snapshots.put(id, snap);
        model.getSpeeds().get(id).addObserver(this);
    }

    @Override
    public void rebind() {
        snapshots.clear();
        bind();
    }

    @Override
    public void unbind(EntityId id) {
        snapshots.remove(id);
        model.getSpeeds().get(id).removeObserver(this);
    }

    @Override
    public void update(EntityId id) {
        SpeedSnapshot snap = model.getSpeeds().get(id).getSnapshot();
        snapshots.put(id, snap);
    }

    public double getSpeed(EntityId id) {
        SpeedSnapshot snap = snapshots.get(id);
        return snap.speed();
    }
}
