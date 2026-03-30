package org.example.cargame.restcontroller;

import org.example.cargame.enums.EngineType;
import org.example.cargame.Servicelayer;
import org.example.cargame.enums.State;
import org.example.cargame.graph.Graph;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/game")
public class GameRestController {
    private final Servicelayer servicelayer;

    public GameRestController(Servicelayer servicelayer) {
        this.servicelayer = servicelayer;
    }

    @PostMapping("/direction")
    public void setDirection(@RequestParam String id) {
        servicelayer.setDirection(id);
    }

    @PostMapping("/color")
    public void setColor(@RequestParam String color) {
        servicelayer.setColor(color);
    }

    @PostMapping("/engine")
    public void setEngine(@RequestParam EngineType engineType) {
        servicelayer.setEngine(engineType);
    }

    @PostMapping("/speed")
    public void setSpeed(@RequestParam double speed) {
        servicelayer.setSpeed(speed);
    }

    @PostMapping("/save")
    public void save(@RequestParam String path) {
        servicelayer.save(path);
    }

    @PostMapping("/load")
    public void load(@RequestParam String path) {
        servicelayer.load(path);
    }

    public List<Integer> getAllIds() {
        return servicelayer.getAllEntities();
    }

    public double getPosX(int id) {
        return servicelayer.getPosX(id);
    }

    public double getPosY(int id) {
        return servicelayer.getPosY(id);
    }

    public String getColor(int id) {
        return servicelayer.getColor(id);
    }

    public EngineType getEngine(int id) {
        return servicelayer.getEngine(id);
    }

    public double getPower(int id) {
        return servicelayer.getPower(id);
    }

    public State getState(int id) {
        return servicelayer.getState(id);
    }

    public String getWarningMessage(int id) {
        return servicelayer.getWarningMessage(id);
    }

    public String getAlertMessage(int id) {
        return servicelayer.getAlertMessage(id);
    }

    public double getSpeed(int id) {
        return servicelayer.getSpeed(id);
    }

    @GetMapping("/graph")
    public java.util.Map<String, Object> getGraph() {
        Graph graph = servicelayer.getGraph();
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        java.util.List<java.util.Map<String, Object>> nodes = new java.util.ArrayList<>();
        for (org.example.cargame.graph.Node n : graph.getNodes()) {
            nodes.add(java.util.Map.of("id", n.getId(), "x", n.getX(), "y", n.getY(), "type", n.getType().toString()));
        }
        java.util.List<java.util.Map<String, Object>> edges = new java.util.ArrayList<>();
        for (org.example.cargame.graph.Edge e : graph.getEdges()) {
            edges.add(java.util.Map.of("source", e.getFrom().getId(), "target", e.getTo().getId()));
        }
        result.put("nodes", nodes);
        result.put("edges", edges);
        return result;
    }

}
