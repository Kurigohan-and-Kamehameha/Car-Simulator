package org.example.cargameFx;

import org.example.cargameFx.entity.Car;
import org.example.cargameFx.graph.Graph;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CarGameFxApplicationTests {

    Car car;

    @Test
    void contextLoads(Graph graph) {

        Dijkstra dji = new Dijkstra();
        dji.calcShortestPath(graph.getNodeById("A"), graph.getNodeById("C"), graph.getNodes());


    }

}
