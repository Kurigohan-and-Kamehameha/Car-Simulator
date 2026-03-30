package org.example.cargame.observer;

import org.example.cargame.CarModel;
import org.example.cargame.entity.EntityId;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ColorView extends ParentView<CarModel> implements ColorObserver {
    private final Map<EntityId, String> colors = new ConcurrentHashMap<>();

    public ColorView(CarModel model) {
        super(model);

        bind();
    }

    @Override
    public void bind() {
        for (EntityId id : model.getAllEntities()) {
            String color = model.getColors().get(id).getColor();
            colors.put(id, color);

            model.getColors().get(id).addObserver(this);
        }
    }

    @Override
    public void bind(EntityId id) {
        String color = model.getColors().get(id).getColor();
        colors.put(id, color);//Lock weil Gameloop kann liste hinzufügen währen UI
        // davor schon getColor aufruft damodel.getColors().get(id) bereits existiert
        model.getColors().get(id).addObserver(this);
    }

    @Override
    public void rebind() {//Lock weil Gameloop kann clearen  währen UI getColor aufruft
        colors.clear();
        bind();
    }

    @Override
    public void unbind(EntityId id) {//Lock weil Gameloop kann remove machen währen UI getColor aufruft
        colors.remove(id);
        model.getColors().get(id).removeObserver(this);
    }

    @Override
    public void update(EntityId id) {
        colors.put(id, model.getColors().get(id).getColor());
    }

    public String getColor(EntityId id) {
        return colors.get(id);
    }

}
