package org.example.cargame.events;

import org.example.cargame.entity.EntityId;
import org.springframework.context.ApplicationEvent;

public class EntityUpdateEvent extends ApplicationEvent {
    public enum Type { UPDATE, REMOVE }

    private final EntityId id;
    private final Type type;

    public EntityUpdateEvent(Object source, EntityId id, Type type) {
        super(source);
        this.id = id;
        this.type = type;
    }

    public EntityId getId() { return id; }
    public Type getType() { return type; }
}