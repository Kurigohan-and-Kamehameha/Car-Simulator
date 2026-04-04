package org.example.cargame.observer;

import org.example.cargame.entity.EntityId;

public interface PushObserver<T> {
    void update(EntityId id, T data);
}
