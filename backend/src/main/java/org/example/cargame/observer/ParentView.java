package org.example.cargame.observer;

import org.example.cargame.Model;
import org.example.cargame.entity.EntityId;

public abstract class ParentView<T extends Model>{
    protected final T model;
    protected final ObserverDispatcher dispatcher;

    protected ParentView(T model, ObserverDispatcher dispatcher) {
        this.model = model;
        this.dispatcher = dispatcher;
    }

    public abstract void bind();
    public abstract void bind(EntityId id);
    public abstract void rebind();
    public abstract void unbind(EntityId id);
}
