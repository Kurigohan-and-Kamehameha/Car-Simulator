package org.example.cargameFx.entity;

import org.example.cargameFx.components.*;

public class Car extends Entity{
    private final PositionComponent pos;
    private final VelocityComponent vel;
    private final ColorComponent col;
    private final EngineComponent engine;
    private final StateComponent state;

    private final Object lock = new Object();

    public Car(PositionComponent pos, VelocityComponent vel, ColorComponent col, EngineComponent engine, StateComponent state){
        this.pos = pos;
        this.vel = vel;
        this.col = col;
        this.engine = engine;
        this.state = state;
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

    public StateComponent getStateComponent() {
        return this.state;
    }

    public Object getLock() {
        return lock;
    }
}
