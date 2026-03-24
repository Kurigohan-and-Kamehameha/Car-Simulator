package org.example.cargameFx.snapshot;

import org.example.cargameFx.graph.Edge;

import java.util.List;

public record PathSnapshot(List<Edge> path) {

    public PathSnapshot(List<Edge> path) {
        this.path = List.copyOf(path);
    }

}
