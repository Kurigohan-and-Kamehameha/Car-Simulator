package org.example.cargame.observer;

import org.example.cargame.CarModel;
import org.example.cargame.entity.EntityId;
import org.example.cargame.snapshot.PositionSnapshot;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PositionView extends ParentView<CarModel> implements PositionObserver {
    private final Map<EntityId, PositionSnapshot> snapshots = new ConcurrentHashMap<>();

    public PositionView(CarModel model) {
        super(model);

        bind();
    }

    @Override
    public void bind() {
        for (EntityId id : model.getAllEntities()) {
            PositionSnapshot snap = model.getPositions().get(id).getSnapshot();
            snapshots.put(id, snap);

            model.getPositions().get(id).addObserver(this);
        }
    }

    @Override
    public void bind(EntityId id) {
        PositionSnapshot snap = model.getPositions().get(id).getSnapshot();
        snapshots.put(id, snap);
        model.getPositions().get(id).addObserver(this);
    }

    @Override
    public void rebind() {
        snapshots.clear();
        bind();
    }

    @Override
    public void unbind(EntityId id) {
        snapshots.remove(id);
        model.getPositions().get(id).removeObserver(this);
    }

    @Override
    public void update(EntityId id) {
        PositionSnapshot snap = model.getPositions().get(id).getSnapshot();
        snapshots.put(id, snap);
    }

    public double getPositionX(EntityId id) {
        PositionSnapshot snap = snapshots.get(id);
        return snap.x();
    }

    public double getPositionY(EntityId id) {
        PositionSnapshot snap = snapshots.get(id);
        return snap.y();
    }
}