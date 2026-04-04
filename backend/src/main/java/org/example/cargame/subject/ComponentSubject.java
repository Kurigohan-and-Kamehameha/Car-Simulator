package org.example.cargame.subject;

import org.example.cargame.entity.EntityId;
import org.example.cargame.observer.PushObserver;

public class ComponentSubject<T> extends Subject<PushObserver<T>> {

    public void notifyObservers(EntityId id, T data) {
        observers.forEach(o -> o.update(id, data));
    }
}
