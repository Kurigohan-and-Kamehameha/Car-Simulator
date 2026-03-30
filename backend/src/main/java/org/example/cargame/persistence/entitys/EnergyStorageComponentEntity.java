package org.example.cargame.persistence.entitys;

import jakarta.persistence.*;

@Entity
@Table(name = "energy_storage_component")
public class EnergyStorageComponentEntity {

    @Id
    private Long id;

    private double power;
    private double capacity;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getPower() { return power; }
    public void setPower(double power) { this.power = power; }

    public double getCapacity() { return capacity; }
    public void setCapacity(double capacity) { this.capacity = capacity; }
}
