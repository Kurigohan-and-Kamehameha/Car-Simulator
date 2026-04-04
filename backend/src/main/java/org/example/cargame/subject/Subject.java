package org.example.cargame.subject;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Subject<O> {
    protected final List<O> observers = new CopyOnWriteArrayList<>();

    public void addObserver(O obs) {
        observers.add(obs);
    }

    public void removeObserver(O obs) {
        observers.remove(obs);
    }

}
