package org.example.cargame.subject;

import org.example.cargame.entity.EntityId;
import org.example.cargame.observer.Observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Subject {
    private final List<Observer> observers = new CopyOnWriteArrayList<>();

    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    public void removeObserver(Observer obs) {
        observers.remove(obs);
    }

    public void notifyObservers(EntityId id) {
        observers.forEach(o -> o.update(id));
    }
}
