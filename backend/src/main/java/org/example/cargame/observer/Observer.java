package org.example.cargame.observer;

import org.example.cargame.entity.EntityId;

public interface Observer {
    void update(EntityId id);
}
