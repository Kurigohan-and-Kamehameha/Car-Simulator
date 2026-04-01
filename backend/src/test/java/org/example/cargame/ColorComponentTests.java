package org.example.cargame;

import org.example.cargame.components.ColorComponent;
import org.example.cargame.entity.EntityId;
import org.example.cargame.observer.ColorView;
import org.example.cargame.observer.ObserverDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ColorComponentTests {

    @Test
    void testSetAndGetColor() {
        ColorComponent component = new ColorComponent();
        component.setColor("red");
        assertEquals("red", component.getColor());

        component.setColor("blue");
        assertEquals("green", component.getColor());
    }

    @Test
    void testColorObserverUpdate() {
        ColorComponent component = new ColorComponent();
        component.setColor("blue");

        ObserverDispatcher dispatcher = new ObserverDispatcher();
        ColorView view = new ColorView(new DummyModel(component), dispatcher);
        EntityId id = new EntityId(0);

        component.setColor("green");
        view.update(id);
        assertEquals("green", view.getColor(id));

        component.setColor("yellow");
        view.update(id);
        assertEquals("yellow", view.getColor(id));
    }

    public static class DummyModel extends CarModel {
        private final EntityId playerId = new EntityId(0);
        private final Map<EntityId, ColorComponent> colors = new HashMap<>();

        public DummyModel(ColorComponent component) {
            colors.put(playerId, component);
        }

        @Override
        public Collection<EntityId> getAllEntities() {
            return java.util.List.of(playerId);
        }

        @Override
        public Map<EntityId, ColorComponent> getColors() {
            return colors;
        }

    }


}
