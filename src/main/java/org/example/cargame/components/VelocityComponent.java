package org.example.cargame.components;

import org.example.cargame.observer.ObserverDispatcher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class VelocityComponent extends org.example.cargame.components.Component {
    private volatile double vx, vy;

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }
}
