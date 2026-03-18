package org.example.cargameFx.PropertyBinding;

import javafx.beans.binding.Binding;

import java.util.ArrayList;
import java.util.List;

import java.util.function.Function;

class Property<T> {
    private T value;
    private final List<Property<?>> boundProperties = new ArrayList<>();

    public Property(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
       // notifyBoundProperties();
    }

    // Bindet diese Property an eine andere Property
    public <S> void bind(Property<S> other, Function<S, T> converter) {
        // Initial synchronisieren
        set(converter.apply(other.get()));
        // Hinzufügen eines Listeners an die andere Property
        other.boundProperties.add(new Property<S>(other.get()) {
            @Override
            public void set(S value) {
                Property.this.set(converter.apply(value));
            }
        });
    }

    private void notifyBindings() {
       // for (Binding<T, ?> b : bindings) {
       //     b.update();
        //}
    }
}