package org.example.cargame.persistence.entitys;

import jakarta.persistence.*;

@Entity
@Table(name = "speed_component")
public class SpeedComponentEntity {

    @Id
    private Long id;

    private double speed;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
}
