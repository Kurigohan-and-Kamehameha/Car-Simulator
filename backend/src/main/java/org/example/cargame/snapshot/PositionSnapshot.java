package org.example.cargame.snapshot;

import org.example.cargame.graph.Edge;
import org.example.cargame.graph.Node;

public record PositionSnapshot(
        Node currentNode,
        Edge currentEdge,
        double edgeProgress,
        double x,
        double y) {

    public PositionSnapshot(Node node) {
        this(node, null, 0.0, node.getX(), node.getY());
    }

}
