package org.example.cargameFx.observer;

import org.example.cargameFx.Model;
import org.example.cargameFx.entity.EntityId;

abstract class PlayerView {
    protected final Model model;
    protected final EntityId playerId;

    public PlayerView(Model model) {
        this.model = model;
        this.playerId = model.getPlayerId();
    }
}
