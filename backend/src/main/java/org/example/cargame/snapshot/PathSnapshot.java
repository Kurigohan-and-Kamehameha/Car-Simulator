package org.example.cargame.snapshot;

import org.example.cargame.graph.Edge;
import java.util.List;

public record PathSnapshot(List<Edge> path, int currentEdgeIndex) {

    public PathSnapshot(List<Edge> path) {
        this(path, 0);
    }

    public int getCurrentEdgeIndex() {
        return currentEdgeIndex;
    }

    public double getWeight(){
        return path.isEmpty() ? Double.POSITIVE_INFINITY
                : path.stream().mapToDouble(Edge::getWeight).sum();
    }

}