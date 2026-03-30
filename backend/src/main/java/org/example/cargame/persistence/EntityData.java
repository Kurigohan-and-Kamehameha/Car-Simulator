package org.example.cargame.persistence;

import org.example.cargame.enums.MessageType;
import org.example.cargame.enums.State;
import org.example.cargame.snapshot.*;

import java.util.Map;

public class EntityData {
    public EnergyStorageSnapshot energy;
    public PathSaveData path;
    public String color;
    public String engineType;
    public PositionSaveData position;
    public SpeedSnapshot speed;
    public State state;
    public Map<MessageType, String> messages;
}
