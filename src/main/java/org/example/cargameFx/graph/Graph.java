package org.example.cargameFx.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final List<Node> nodes;
    private final List<Edge> edges;

    public Graph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void addEdge(Node from, Node to, double weight) {
        Edge edge = new Edge(from, to, weight);
        edges.add(edge);
        from.addEdge(edge);
        to.addEdge(edge);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public Node getNodeById(String id) {
        for (Node node : nodes) {
            if (node.getId().equals(id)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Edge edge : edges) {
            sb.append(edge).append("\n");
        }
        return sb.toString();
    }
}
