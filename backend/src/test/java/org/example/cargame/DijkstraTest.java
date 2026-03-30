package org.example.cargame;

import org.example.cargame.graph.Edge;
import org.example.cargame.graph.Graph;
import org.example.cargame.graph.Node;
import org.example.cargame.snapshot.PathSnapshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DijkstraTest {
    private Graph graphA, graphB, graphC;
    private Dijkstra dijkstra;

    @BeforeEach
    void setup() {
        dijkstra = new Dijkstra();

        // ---------- Graph A ----------
        graphA = new Graph();

        Node a1 = new Node("A", 400, 200, null);
        Node b1 = new Node("B", 40, 40, null);
        Node c1 = new Node("C", 800, 500, null);
        Node d1 = new Node("D", 100, 500, null);

        graphA.addNode(a1);
        graphA.addNode(b1);
        graphA.addNode(c1);
        graphA.addNode(d1);

        graphA.addEdge(a1, b1);
        graphA.addEdge(b1, c1);
        graphA.addEdge(b1, d1);
        graphA.addEdge(c1, a1);
        graphA.addEdge(c1, d1);

        // ---------- Graph B ----------
        graphB = new Graph();

        Node a2 = new Node("A", 400, 200, null);
        Node b2 = new Node("B", 40, 40, null);
        Node c2 = new Node("C", 800, 500, null);

        graphB.addNode(a2);
        graphB.addNode(b2);
        graphB.addNode(c2);

        graphB.addEdge(a2, c2);

        // ---------- Graph C ----------
        graphC = new Graph();

        Node a3 = new Node("A", 400, 200, null);
        Node b3 = new Node("B", 40, 40, null);

        graphC.addNode(a3);
        graphC.addNode(b3);
    }

    @Test
    void testNoEdges() {
        Node start = graphC.getNodeById("A");
        Node target = graphC.getNodeById("B");
        List<Edge> path = dijkstra.calcShortestPath(start, target, graphC.getNodes());

        assertNotNull(path);
        assertTrue(path.isEmpty());

        PathSnapshot newSnap = new PathSnapshot(path);
        double totalWeight = newSnap.getWeight();
        assertEquals(Double.POSITIVE_INFINITY, totalWeight);
    }

    @Test
    void testNoValidPath() {
        Node start = graphB.getNodeById("A");
        Node target = graphB.getNodeById("B");
        List<Edge> path = dijkstra.calcShortestPath(start, target, graphB.getNodes());

        assertNotNull(path);
        assertTrue(path.isEmpty());

        PathSnapshot newSnap = new PathSnapshot(path);
        double totalWeight = newSnap.getWeight();
        assertEquals(Double.POSITIVE_INFINITY, totalWeight);
    }

    @Test
    void testShortestPathFromAToD() {
        Node start = graphA.getNodeById("A");
        Node target = graphA.getNodeById("D");
        List<Edge> path = dijkstra.calcShortestPath(start, target, graphA.getNodes());

        assertNotNull(path);
        assertFalse(path.isEmpty());

        assertEquals("A", path.get(0).getFrom().getId());
        assertEquals("B", path.get(0).getTo().getId());
        assertEquals("B", path.get(1).getFrom().getId());
        assertEquals("D", path.get(1).getTo().getId());
    }

    @Test
    void testWeightAtoD() {
        Node start = graphA.getNodeById("A");
        Node target = graphA.getNodeById("D");
        List<Edge> path = dijkstra.calcShortestPath(start, target, graphA.getNodes());

        double totalWeight = path.stream().mapToDouble(Edge::getWeight).sum();
        Edge abEdge = graphA.getNodeById("A").getEdges().stream()
                .filter(e -> (e.getFrom().getId().equals("B") || e.getTo().getId().equals("B")))
                .findFirst() // Nur die erste Kante zwischen A und B
                .orElseThrow(() -> new IllegalArgumentException("Keine Kante zwischen A und B gefunden"));
        Edge bdEdge = graphA.getNodeById("B").getEdges().stream()
                .filter(e -> (e.getFrom().getId().equals("D") || e.getTo().getId().equals("D")))
                .findFirst() // Nur die erste Kante zwischen A und B
                .orElseThrow(() -> new IllegalArgumentException("Keine Kante zwischen B und D gefunden"));
        double expectedWeight = abEdge.getWeight() + bdEdge.getWeight();
        assertEquals(expectedWeight, totalWeight, 0.0001);
    }

    @Test
    void testBidirectional() {
        Node start = graphA.getNodeById("A");
        Node target = graphA.getNodeById("C");
        List<Edge> path = dijkstra.calcShortestPath(start, target, graphA.getNodes());

        assertNotNull(path);
        assertFalse(path.isEmpty());

        assertEquals("A", path.getFirst().getFrom().getId());
        assertEquals("C", path.getFirst().getTo().getId());

        start = graphA.getNodeById("C");
        target = graphA.getNodeById("A");
        path = dijkstra.calcShortestPath(start, target, graphA.getNodes());

        assertNotNull(path);
        assertFalse(path.isEmpty());

        assertEquals("C", path.getFirst().getFrom().getId());
        assertEquals("A", path.getFirst().getTo().getId());
    }

}

