package org.example.cargameFx;

import org.example.cargameFx.components.*;
import org.example.cargameFx.entity.EntityId;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Model {
    private final Map<EntityId, PositionComponent> positions = new ConcurrentHashMap<>();
    private final Map<EntityId, VelocityComponent> velocities = new ConcurrentHashMap<>();
    private final Map<EntityId, ColorComponent> colors = new ConcurrentHashMap<>();
    private final Map<EntityId, EngineComponent> engines = new ConcurrentHashMap<>();
    private final Map<EntityId, StateComponent> states = new ConcurrentHashMap<>();
    private final Map<EntityId, SpeedComponent> speeds = new ConcurrentHashMap<>();

    private final AtomicInteger nextId = new AtomicInteger();
    private EntityId playerId;

    public EntityId createEntity() {
        return new EntityId(nextId.getAndIncrement());
    }

    public void removeEntity(EntityId id) {
        positions.remove(id);
        velocities.remove(id);
        colors.remove(id);
        engines.remove(id);
        states.remove(id);
        speeds.remove(id);
    }

    public void setPlayer(EntityId id) {
        this.playerId = id;
    }

    public EntityId getPlayerId() {
        return playerId;
    }

    public Iterable<EntityId> getAllEntities() {
        return positions.keySet();
    }

    public Map<EntityId, PositionComponent> getPositions() { return positions; }
    public Map<EntityId, VelocityComponent> getVelocities() { return velocities; }
    public Map<EntityId, ColorComponent> getColors() { return colors; }
    public Map<EntityId, EngineComponent> getEngines() { return engines; }
    public Map<EntityId, StateComponent> getStates() { return states; }
    public Map<EntityId, SpeedComponent> getSpeeds() { return speeds; }
}
