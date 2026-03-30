package org.example.cargame.persistence.entitys;

import jakarta.persistence.*;

@Entity
@Table(name = "position_component")
public class PositionComponentEntity {

    @Id
    private Long id;

    private String currentNodeId;
    private String currentEdgeFromId;
    private String currentEdgeToId;
    private double edgeProgress;
    private double x;
    private double y;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCurrentNodeId() { return currentNodeId; }
    public void setCurrentNodeId(String currentNodeId) { this.currentNodeId = currentNodeId; }

    public String getCurrentEdgeFromId() { return currentEdgeFromId; }
    public void setCurrentEdgeFromId(String currentEdgeFromId) { this.currentEdgeFromId = currentEdgeFromId; }

    public String getCurrentEdgeToId() { return currentEdgeToId; }
    public void setCurrentEdgeToId(String currentEdgeToId) { this.currentEdgeToId = currentEdgeToId; }

    public double getEdgeProgress() { return edgeProgress; }
    public void setEdgeProgress(double edgeProgress) { this.edgeProgress = edgeProgress; }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
}
