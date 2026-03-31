package org.example.cargame.observer;

import org.example.cargame.CarModel;
import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.ActionType;
import org.example.cargame.events.EntityUpdateEvent;
import org.example.cargame.snapshot.PositionSnapshot;
import org.springframework.context.ApplicationEventPublisher;
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
        model.getPositions().get(id).removeObserver(this);
        dispatcher.dispatch(() -> GameStateRegistry.notify(id, ActionType.REMOVE));
    }

    @Override
    public void update(EntityId id) {
        cache.put(id, model.getPositions().get(id).getSnapshot());
        dispatcher.dispatch(() -> GameStateRegistry.notify(id, ActionType.UPDATE));
    }

    public double getPositionX(EntityId id) {
        PositionSnapshot snap = cache.get(id);
        return snap != null ? snap.x() : 0;
    }

    public double getPositionY(EntityId id) {
        PositionSnapshot snap = cache.get(id);
        return snap != null ? snap.y() : 0;
    }
}