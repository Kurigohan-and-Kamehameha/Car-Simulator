package org.example.cargame.observer;

import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.ActionType;
import org.example.cargame.snapshot.GameStateDTO;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameStateView {
    private final Map<EntityId, GameStateDTO> cache = new ConcurrentHashMap<>();

    private final PositionView positionView;
    private final StateView stateView;
    private final SpeedView speedView;
    private final StorageView storageView;
    private final EngineView engineView;
    private final ColorView colorView;
    private final MessageView messageView;

    public GameStateView(
            PositionView positionView,
            StateView stateView,
            SpeedView speedView,
            StorageView storageView,
            EngineView engineView,
            ColorView colorView,
            MessageView messageView,
            ObserverDispatcher dispatcher
    ) {
        this.positionView = positionView;
        this.stateView = stateView;
        this.speedView = speedView;
        this.storageView = storageView;
        this.engineView = engineView;
        this.colorView = colorView;
        this.messageView = messageView;
    }

    public GameStateDTO get(EntityId id) {
        return cache.get(id);
    }

    public Map<EntityId, GameStateDTO> getAll() {
        return Map.copyOf(cache);
    }

    public void handleUpdateFromDispatcher(EntityId id, ActionType action) {
        switch (action) {
            case UPDATE -> updateEntity(id);
            case REMOVE -> cache.remove(id);
        }
    }

    public void updateEntity(EntityId id) {
        GameStateDTO dto = new GameStateDTO(
                positionView.getPositionX(id),
                positionView.getPositionY(id),
                colorView.getColor(id),
                engineView.getEngineType(id),
                storageView.getPower(id),
                stateView.getState(id),
                messageView.alert(id),
                messageView.warning(id),
                speedView.getSpeed(id)
        );
        cache.put(id, dto);
    }

    public void remove(EntityId id) {
        cache.remove(id);
    }
}
