package org.example.cargameFx.graph;

import java.util.*;

public class Graph {
    private final Map<String, Node> nodesById = new HashMap<>();
    private final List<Edge> edges = new ArrayList<>();

    public void addNode(Node node) {
        nodesById.put(node.getId(), node);
    }

    public void addEdge(Node from, Node to) {
        Edge edge = new Edge(from, to);
        edges.add(edge);
        from.addEdge(edge);
        to.addEdge(edge);
    }

    public Collection<Node> getNodes() {
        return nodesById.values();
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public Node getNodeById(String id) {
        return nodesById.get(id);
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
