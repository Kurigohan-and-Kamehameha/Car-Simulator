package org.example.cargame.persistence;

import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.EngineType;
import org.example.cargame.enums.MessageType;
import org.example.cargame.enums.State;
import org.example.cargame.snapshot.*;

import java.util.HashMap;
import java.util.Map;

public final class LoadedGameData {
    public final Map<EntityId, Double> speeds = new HashMap<>();
    public final Map<EntityId, PositionSnapshot> positions = new HashMap<>();
    public final Map<EntityId, String> colors = new HashMap<>();
    public final Map<EntityId, EngineType> engines = new HashMap<>();
    public final Map<EntityId, PathSnapshot> paths = new HashMap<>();
    public final Map<EntityId, Map<MessageType, String>> messages = new HashMap<>();
    public final Map<EntityId, EnergyStorageSnapshot> storage = new HashMap<>();
    public final Map<EntityId, State> states = new HashMap<>();

}
