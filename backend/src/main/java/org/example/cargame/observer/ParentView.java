package org.example.cargame.observer;

import org.example.cargame.Model;
import org.example.cargame.subject.ViewSubject;
import org.springframework.stereotype.Component;

@Component
public abstract class ParentView<T extends Model> extends ViewSubject implements Bindable{
    protected final T model;
    protected final ObserverDispatcher dispatcher;

    protected ParentView(T model, ObserverDispatcher dispatcher) {
        this.model = model;
        this.dispatcher = dispatcher;
    }

}
