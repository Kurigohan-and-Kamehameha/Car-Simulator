package org.example.cargameFx.config;

import org.example.cargameFx.enums.NodeType;
import org.example.cargameFx.graph.Graph;
import org.example.cargameFx.graph.Node;
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

        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);
        graph.addNode(e);
        graph.addNode(f);
        graph.addNode(g);

        graph.addEdge(a, b);
        graph.addEdge(b, c);
        graph.addEdge(b, d);
        graph.addEdge(c, a);
        graph.addEdge(b, e);
        graph.addEdge(e, f);
        graph.addEdge(f, d);
        graph.addEdge(c, d);
        graph.addEdge(g, e);

        System.out.println(graph.toString());

        return graph;
    }
}
