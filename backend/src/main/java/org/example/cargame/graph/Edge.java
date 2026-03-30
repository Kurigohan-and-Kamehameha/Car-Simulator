package org.example.cargame.graph;

public class Edge {
    private final Node from;
    private final Node to;
    private double weight;

    public Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
        calculate();
    }

    private void calculate() {
        this.weight = Math.hypot(
                this.getTo().getX() - this.getFrom().getX(),
                this.getTo().getY() - this.getFrom().getY()
        );
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return from + " - " + to + " (weight: " + weight + ")";
    }

}
