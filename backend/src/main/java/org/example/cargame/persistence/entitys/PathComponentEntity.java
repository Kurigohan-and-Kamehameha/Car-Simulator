package org.example.cargame.persistence.entitys;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "path_component")
public class PathComponentEntity {

    @Id
    private Long id;

    @ElementCollection
    @CollectionTable(name = "path_edges_from", joinColumns = @JoinColumn(name = "path_id"))
    @Column(name = "edge_id_from")
    private List<String> edgeIdsFrom;

    @ElementCollection
    @CollectionTable(name = "path_edges_to", joinColumns = @JoinColumn(name = "path_id"))
    @Column(name = "edge_id_to")
    private List<String> edgeIdsTo;

    private int currentEdgeIndex;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public List<String> getEdgeIdsFrom() { return edgeIdsFrom; }
    public void setEdgeIdsFrom(List<String> edgeIdsFrom) { this.edgeIdsFrom = edgeIdsFrom; }

    public List<String> getEdgeIdsTo() { return edgeIdsTo; }
    public void setEdgeIdsTo(List<String> edgeIdsTo) { this.edgeIdsTo = edgeIdsTo; }

    public void setCurrentEdgeIndex(int currentEdgeIndex) { this.currentEdgeIndex = currentEdgeIndex; }
    public int getCurrentEdgeIndex() { return this.currentEdgeIndex; }

}
