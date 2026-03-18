package org.example.cargameFx;

import org.example.cargameFx.enums.NodeType;
import org.example.cargameFx.graph.Graph;
import org.example.cargameFx.graph.Node;

public class World {
    public World(){
        Graph graph = new Graph();

        Node a = new Node("A", 400, 200, NodeType.WORKSOP);
        Node b = new Node("B", 40, 40, NodeType.INTERSECTION);
        Node c = new Node("C", 800,500, NodeType.GASSTATION);

        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);

        graph.addEdge(a, b, 5);
        graph.addEdge(b, c, 3);
        graph.addEdge(a, c, 10);

        System.out.println(graph);
    }
}
