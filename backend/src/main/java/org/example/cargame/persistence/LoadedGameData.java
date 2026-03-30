package org.example.cargame.persistence;

import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.EngineType;
import org.example.cargame.enums.MessageType;
import org.example.cargame.enums.State;
import org.example.cargame.snapshot.EnergyStorageSnapshot;
import org.example.cargame.snapshot.PathSnapshot;
import org.example.cargame.snapshot.PositionSnapshot;
import org.example.cargame.snapshot.SpeedSnapshot;

import java.util.HashMap;
import java.util.Map;

public class LoadedGameData {
    public Map<EntityId, SpeedSnapshot> speeds = new HashMap<>();
    public Map<EntityId, PositionSnapshot> positions = new HashMap<>();
    public Map<EntityId, String> colors = new HashMap<>();
    public Map<EntityId, EngineType> engines = new HashMap<>();
    public Map<EntityId, PathSnapshot> paths = new HashMap<>();
    public Map<EntityId, Map<MessageType, String>> messages = new HashMap<>();
    public Map<EntityId, EnergyStorageSnapshot> storage = new HashMap<>();
    public Map<EntityId, State> states = new HashMap<>();
}
