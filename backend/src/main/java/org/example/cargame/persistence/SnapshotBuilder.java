package org.example.cargame.persistence;

import org.example.cargame.CarModel;

public class SnapshotBuilder {

    public LoadedGameData build(CarModel model) {
        LoadedGameData data = new LoadedGameData();

        model.getSpeeds().forEach((id, comp) -> {
            if (comp != null)
                data.speeds.put(id, comp.getSnapshot());
        });

        model.getPositions().forEach((id, comp) -> {
            if (comp != null)
                data.positions.put(id, comp.getSnapshot());
        });

        model.getColors().forEach((id, comp) -> {
            if (comp != null)
                data.colors.put(id, comp.getColor());
        });

        model.getEngines().forEach((id, comp) -> {
            if (comp != null)
                data.engines.put(id, comp.getActiveEngine().getType());
        });

        model.getMessages().forEach((id, comp) -> {
            if (comp != null)
                data.messages.put(id, comp.getMessages());
        });

        model.getStorage().forEach((id, comp) -> {
            if (comp != null)
                data.storage.put(id, comp.getSnapshot());
        });

        model.getStates().forEach((id, comp) -> {
            if (comp != null)
                data.states.put(id, comp.get());
        });

        model.getPaths().forEach((id, comp) -> {
            if (comp != null)
                data.paths.put(id, comp.getSnapshot());
        });

        return data;
    }
}
