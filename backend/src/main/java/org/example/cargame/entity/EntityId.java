package org.example.cargame.entity;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class EntityId {
    private final int id;
    private final ReentrantLock lock = new ReentrantLock();

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

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }
}
