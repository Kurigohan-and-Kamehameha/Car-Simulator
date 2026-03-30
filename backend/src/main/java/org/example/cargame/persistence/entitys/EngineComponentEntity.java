package org.example.cargame.persistence.entitys;

import jakarta.persistence.*;

@Entity
@Table(name = "engine_component")
public class EngineComponentEntity {

    @Id
    private Long id;

    @Column(name = "engine_type")
    private String engineType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }
}
