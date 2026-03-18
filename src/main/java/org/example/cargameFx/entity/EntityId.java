package org.example.cargameFx.entity;

import java.util.Objects;

public class EntityId {
    private final int id;

    public EntityId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityId entityId)) return false;
        return id == entityId.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "EntityId{" + id + '}';
    }
}
