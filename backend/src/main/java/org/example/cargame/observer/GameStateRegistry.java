package org.example.cargame.observer;

import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.ActionType;
import org.springframework.stereotype.Component;

@Component
public class GameStateRegistry {

    private static GameStateView gameStateView;

    public GameStateRegistry(GameStateView view) {
        gameStateView = view;
    }

    public static void notify(EntityId id, ActionType action) {
        gameStateView.handleUpdateFromDispatcher(id, action);
    }
}
