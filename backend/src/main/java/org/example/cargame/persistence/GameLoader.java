package org.example.cargame.persistence;

import org.example.cargame.CarModel;
import org.example.cargame.components.*;
import org.example.cargame.entity.EntityId;
import org.example.cargame.factories.EngineFactory;
import org.example.cargame.snapshot.ColorSnapshot;
import org.example.cargame.snapshot.SpeedSnapshot;
import org.example.cargame.snapshot.MessageSnapshot;
import org.example.cargame.snapshot.StateSnapshot;

public class GameLoader {

    public void apply(LoadedGameData data, CarModel model, EngineFactory engineFactory) {

        data.speeds.forEach((id, snap) -> model.removeEntity(id));

        data.speeds.forEach((id, speed) -> {
            SpeedComponent comp = new SpeedComponent();
            comp.setSnapshot(new SpeedSnapshot(speed));
            model.getSpeeds().put(id, comp);
        });

        data.positions.forEach((id, snap) -> {
            PositionComponent comp = new PositionComponent();
            comp.setSnapshot(snap);
            model.getPositions().put(id, comp);
        });

        data.colors.forEach((id, colorStr) -> {
            ColorComponent comp = new ColorComponent();
            comp.setSnapshot(new ColorSnapshot(colorStr));
            model.getColors().put(id, comp);
        });

        data.engines.forEach((id, engineType) -> {
            EngineComponent comp = engineFactory.create();
            comp.setEngine(engineType);
            model.getEngines().put(id, comp);
        });

        data.paths.forEach((id, path) -> {
            PathComponent comp = new PathComponent();
            comp.setSnapshot(path);
            model.getPaths().put(id, comp);
        });

        data.messages.forEach((id, msgMap) -> {
            MessageComponent comp = new MessageComponent();
            comp.setSnapshot(new MessageSnapshot(msgMap));
            model.getMessages().put(id, comp);
        });

        data.storage.forEach((id, snap) -> {
            EnergyStorageComponent comp = new EnergyStorageComponent();
            comp.setSnapshot(snap);
            model.getStorage().put(id, comp);
        });

        data.states.forEach((id, state) -> {
            StateComponent comp = new StateComponent();
            comp.setSnapshot(new StateSnapshot(state));
            model.getStates().put(id, comp);
        });

        int maxId = data.speeds.keySet().stream()
                .mapToInt(EntityId::getId)
                .max()
                .orElse(0);

        model.resetNextId(maxId + 1);
    }

}
