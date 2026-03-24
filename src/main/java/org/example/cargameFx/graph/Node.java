package org.example.cargameFx.graph;

import org.example.cargameFx.enums.NodeType;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final NodeType type;
    private final String id;
    private final double x;
    private final double y;
    private final List<Edge> edges;

    public Node(String id, double x, double y, NodeType type) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.type = type;
        this.edges = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public NodeType getType() {
        return type;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public List<String> getNeighbors(Node currentNode){
        List<String> nodeIdList = new ArrayList<>();
        for (Edge edge : currentNode.getEdges()) {
            Node neighbor = edge.getFrom().equals(currentNode) ? edge.getTo() : edge.getFrom();
            nodeIdList.add(neighbor.getId());
        }
        return nodeIdList;
    }

    @Override
    public String toString() {
        return this.id;
    }

}
