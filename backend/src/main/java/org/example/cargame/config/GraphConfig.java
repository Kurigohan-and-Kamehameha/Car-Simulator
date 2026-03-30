package org.example.cargame.config;

import org.example.cargame.enums.NodeType;
import org.example.cargame.graph.Graph;
import org.example.cargame.graph.Node;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphConfig {

    @Bean
    public Graph createGraph() {
        Graph graph = new Graph();

        Node a = new Node("A", 400, 200, NodeType.WORKSHOP);
        Node b = new Node("B", 40, 40, NodeType.INTERSECTION);
        Node c = new Node("C", 800, 500, NodeType.GASSTATION);
        Node d = new Node("D", 100, 500, NodeType.INTERSECTION);
        Node e = new Node("E", 800, 40, NodeType.INTERSECTION);
        Node f = new Node("F", 800, 400, NodeType.INTERSECTION);
        Node g = new Node("G", 600, 150, NodeType.GASSTATION);
        Node h = new Node("H", 100, 600, NodeType.INTERSECTION);
        Node i = new Node("I", 400, 700, NodeType.GASSTATION);
        Node j = new Node("J", 900, 150, NodeType.INTERSECTION);
        Node k = new Node("K", 300, 300, NodeType.WORKSHOP);
        Node l = new Node("L", 900, 580, NodeType.INTERSECTION);

        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);
        graph.addNode(e);
        graph.addNode(f);
        graph.addNode(g);
        graph.addNode(h);
        graph.addNode(i);
        graph.addNode(j);
        graph.addNode(k);
        graph.addNode(l);

        graph.addEdge(a, b);
        graph.addEdge(b, c);
        graph.addEdge(b, d);
        graph.addEdge(a, c);
        graph.addEdge(b, e);
        graph.addEdge(e, f);
        graph.addEdge(f, d);
        graph.addEdge(c, d);
        graph.addEdge(g, e);
        graph.addEdge(l, f);
        graph.addEdge(l, i);
        graph.addEdge(h, i);
        graph.addEdge(k, h);
        graph.addEdge(k, i);
        graph.addEdge(h, d);
        graph.addEdge(e, j);

        System.out.println(graph.toString());

        return graph;
    }
}
