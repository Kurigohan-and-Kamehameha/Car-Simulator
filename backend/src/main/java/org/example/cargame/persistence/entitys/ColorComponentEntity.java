package org.example.cargame.persistence.entitys;

import jakarta.persistence.*;

@Entity
@Table(name = "color_component")
public class ColorComponentEntity {

    @Id
    private Long id;
    private String color;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
