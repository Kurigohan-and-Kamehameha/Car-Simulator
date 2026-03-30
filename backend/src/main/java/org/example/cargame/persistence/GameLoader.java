package org.example.cargame.persistence;

import org.example.cargame.CarModel;
import org.example.cargame.components.*;
import org.example.cargame.entity.EntityId;
import org.example.cargame.factories.EngineFactory;
import org.example.cargame.snapshot.PathSnapshot;

import java.util.ArrayList;

public class GameLoader {

    public void apply(LoadedGameData data, CarModel model, EngineFactory engineFactory) {

        data.speeds.forEach((id, snap) -> {
            model.removeEntity(id);
        });

        data.speeds.forEach((id, snap) -> {
            SpeedComponent comp = new SpeedComponent();
            comp.setSnapshot(snap);
            model.getSpeeds().put(id, comp);
        });

        data.positions.forEach((id, snap) -> {
            PositionComponent comp = new PositionComponent();
            comp.setSnapshot(snap);
            model.getPositions().put(id, comp);
        });

        data.colors.forEach((id, color) -> {
            ColorComponent comp = new ColorComponent();
            comp.setColor(color);
            model.getColors().put(id, comp);
        });

        data.engines.forEach((id, type) -> {
            EngineComponent comp = engineFactory.create();
            comp.setEngine(type);
            model.getEngines().put(id, comp);
        });

        data.paths.forEach((id, path) -> {
            PathComponent comp = new PathComponent();
            comp.setSnapshot(path);
            model.getPaths().put(id, comp);
        });
        /*
        for (EntityId id : model.getAllEntities()) {
            if(data.colors.get(id) != null){
                PathSnapshot snap = data.paths.get(id);
                if (snap == null) {
                    snap = new PathSnapshot(new ArrayList<>(), 0);
                }
                PathComponent comp = new PathComponent();
                comp.setSnapshot(snap);
                model.getPaths().put(id, comp);
            }
        }
        */
        data.messages.forEach((id, msgs) -> {
            MessageComponent comp = new MessageComponent();
            comp.setMessages(msgs);
            model.getMessages().put(id, comp);
        });

        data.storage.forEach((id, snap) -> {
            EnergyStorageComponent comp = new EnergyStorageComponent();
            comp.setSnapshot(snap);
            model.getStorage().put(id, comp);
        });

        data.states.forEach((id, state) -> {
            StateComponent comp = new StateComponent();
            comp.set(state);
            model.getStates().put(id, comp);
        });

        int maxId = data.speeds.keySet().stream()
                .mapToInt(EntityId::getId)
                .max()
                .orElse(0);

        model.resetNextId(maxId + 1);
    }

}
