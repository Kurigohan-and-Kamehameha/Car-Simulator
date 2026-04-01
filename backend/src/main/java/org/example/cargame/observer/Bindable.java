package org.example.cargame.observer;

import org.example.cargame.entity.EntityId;

public interface Bindable {
    void bind();
    void bind(EntityId id);
    void rebind();
    void unbind(EntityId id);
}
