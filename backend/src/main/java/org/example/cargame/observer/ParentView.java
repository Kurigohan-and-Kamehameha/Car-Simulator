package org.example.cargame.observer;

import org.example.cargame.Model;
import org.example.cargame.entity.EntityId;

import java.util.concurrent.locks.Lock;

public abstract class ParentView<T extends Model>{
    protected final T model;


    protected ParentView(T model) {
        this.model = model;
    }
    public abstract void bind();
    public abstract void bind(EntityId id);
    public abstract void rebind();
    public abstract void unbind(EntityId id);
}
