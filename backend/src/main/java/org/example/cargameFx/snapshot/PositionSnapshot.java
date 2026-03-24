package org.example.cargameFx.snapshot;

import org.example.cargameFx.graph.Edge;
import org.example.cargameFx.graph.Node;

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
