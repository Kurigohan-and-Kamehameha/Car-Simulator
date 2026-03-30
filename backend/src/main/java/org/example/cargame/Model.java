package org.example.cargame;

import org.example.cargame.components.PositionComponent;
import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.ModelType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Model {
    protected ModelType type;
    private final Map<EntityId, Boolean> binds = new ConcurrentHashMap<>();
    protected final AtomicInteger nextId = new AtomicInteger();

    public EntityId createEntity() {
        EntityId id = new EntityId(nextId.getAndIncrement());
        return new EntityId(nextId.getAndIncrement());
    }
    public void resetNextId(int value) {
        nextId.set(value);
    }

    public abstract void clear();
    public abstract void removeEntity(EntityId id);
    public abstract ModelType getType();

    public void bind(EntityId id, boolean boo){
        this.binds.put(id, boo);
    }
    public boolean isBind(EntityId id) {
        return this.binds.get(id);
    }

}
