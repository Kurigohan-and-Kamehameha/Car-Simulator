package org.example.cargame.persistence.entitys;

import jakarta.persistence.*;
import org.example.cargame.enums.State;

@Entity
@Table(name = "state_component")
public class StateComponentEntity {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
