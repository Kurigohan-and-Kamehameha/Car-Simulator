package org.example.cargame.observer;

import org.example.cargame.CarModel;
import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.State;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StateView extends ParentView<CarModel> implements StateObserver {
    private final Map<EntityId, State> states = new ConcurrentHashMap<>();

    public StateView(CarModel model) {
        super(model);

        bind();
    }

    @Override
    public void bind() {
        for (EntityId id : model.getAllEntities()) {
            State state = model.getStates().get(id).get();
            states.put(id, state);

            model.getStates().get(id).addObserver(this);
        }
    }

    @Override
    public void bind(EntityId id) {
        State state = model.getStates().get(id).get();
        states.put(id, state);
        model.getStates().get(id).addObserver(this);
    }

    @Override
    public void rebind() {
        states.clear();
        bind();
    }

    @Override
    public void unbind(EntityId id) {
        states.remove(id);
        model.getStates().get(id).removeObserver(this);
    }

    @Override
    public void update(EntityId id) {
        states.put(id, model.getStates().get(id).get());
    }

    public State getState(EntityId id) {
        return states.get(id);
    }
}
