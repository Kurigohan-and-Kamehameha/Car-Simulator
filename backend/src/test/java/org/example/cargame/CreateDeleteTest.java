package org.example.cargame;

import org.example.cargame.graph.Graph;
import org.example.cargame.graph.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CreateDeleteTest {

    @Autowired
    private Servicelayer servicelayer;

    @Autowired
    private CarModel model;

    @BeforeEach
    void resetModel() {
        model.clear();
    }

    @Test
    void testSetAndGetColor() {

        Graph graph = new Graph();
        Node a = new Node("A", 400, 200, null);
        graph.addNode(a);

        servicelayer.createEntity("A");
        List<Integer> entitiesAfterCreate = servicelayer.getAllEntities();

        servicelayer.removeEntity(entitiesAfterCreate.getFirst());
        List<Integer> entitiesAfterRemove = servicelayer.getAllEntities();

        assertEquals(1, entitiesAfterCreate.size());
        assertEquals(0, entitiesAfterRemove.size());
    }

}