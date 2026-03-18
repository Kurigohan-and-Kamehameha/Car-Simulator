package org.example.cargameFx.entity;

import org.example.cargameFx.components.*;

public class Car extends Entity{
    private final PositionComponent pos;
    private final VelocityComponent vel;
    private final ColorComponent col;
    private final EngineComponent engine;
    private final StateComponent state;
    private final SpeedComponent speed;

    private final Object lock = new Object();

    public Car(PositionComponent pos, VelocityComponent vel, SpeedComponent speed, ColorComponent col, EngineComponent engine, StateComponent state){
        this.pos = pos;
        this.vel = vel;
        this.col = col;
        this.engine = engine;
        this.state = state;
        this.speed = speed;
    }

    public PositionComponent getPos() {
        return this.pos;
    }

    public VelocityComponent getVel() {
        return this.vel;
    }

    public ColorComponent getCol() {
        return this.col;
    }

    public EngineComponent getEngineComponent() {
        return this.engine;
    }

    public SpeedComponent getSpeedComponent() {
        return this.speed;
    }

    public StateComponent getStateComponent() {
        return this.state;
    }

    public Object getLock() {
        return lock;
    }
}
