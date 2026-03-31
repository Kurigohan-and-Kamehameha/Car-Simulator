package org.example.cargame.observer;

import org.example.cargame.CarModel;
import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.ActionType;
import org.example.cargame.enums.State;
import org.example.cargame.events.EntityUpdateEvent;
import org.example.cargame.snapshot.SpeedSnapshot;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StateView extends ParentView<CarModel> implements StateObserver {
    private final Map<EntityId, State> cache = new ConcurrentHashMap<>();

    public StateView(CarModel model, ObserverDispatcher dispatcher) {
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
        State state = model.getStates().get(id).get();
        cache.put(id, state);
        model.getStates().get(id).addObserver(this);
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
        model.getStates().get(id).removeObserver(this);
        dispatcher.dispatch(() -> GameStateRegistry.notify(id, ActionType.REMOVE));
    }

    @Override
    public void update(EntityId id) {
        cache.put(id, model.getStates().get(id).get());
        dispatcher.dispatch(() -> GameStateRegistry.notify(id, ActionType.UPDATE));
    }

    public State getState(EntityId id) {
        return cache.get(id);
    }
}
