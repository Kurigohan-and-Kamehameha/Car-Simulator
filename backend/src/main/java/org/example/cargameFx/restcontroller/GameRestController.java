package org.example.cargameFx.restcontroller;

import org.example.cargameFx.enums.EngineType;
import org.example.cargameFx.Servicelayer;
import org.example.cargameFx.enums.State;
import org.example.cargameFx.graph.Graph;
import org.example.cargameFx.observer.EngineView;
import org.example.cargameFx.observer.ColorView;
import org.example.cargameFx.observer.PositionView;
import org.example.cargameFx.observer.SpeedView;
import org.example.cargameFx.observer.StateView;
import org.example.cargameFx.observer.StorageView;
import org.example.cargameFx.observer.MessageView;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin
@RequestMapping("/api/game")
public class GameRestController {
    private final Servicelayer servicelayer;
    private final ColorView viewColer;
    private final EngineView viewEngine;
    private final PositionView viewPos;
    private final StateView viewState;
    private final StorageView storageView;
    private final Graph graph;
    private final MessageView viewMessage;
    private final SpeedView viewSpeed;

    public GameRestController(Servicelayer servicelayer, ColorView viewColer, EngineView viewEngine,
            PositionView viewPos, SpeedView viewSpeed,
            StateView viewState, StorageView storageView, Graph graph, MessageView viewMessage) {
        this.servicelayer = servicelayer;
        this.viewColer = viewColer;
        this.viewEngine = viewEngine;
        this.viewPos = viewPos;
        this.viewSpeed = viewSpeed;
        this.viewState = viewState;
        this.storageView = storageView;
        this.graph = graph;
        this.viewMessage = viewMessage;
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

    public double getPosX() {
        return viewPos.getPositionX();
    }

    public double getPosY() {
        return viewPos.getPositionY();
    }

    public String getColor() {
        return viewColer.getColor();
    }

    public EngineType getEngine() {
        return viewEngine.getEngineType();
    }

    public double getPower() {
        return storageView.getPower();
    }

    public State getState() {
        return viewState.getState();
    }

    public String getWarningMessage() {
        return viewMessage.warning();
    }

    public String getAlertMessage() {
        return viewMessage.alert();
    }

    public double getSpeed() {
        return viewSpeed.getSpeed();
    }

    @GetMapping("/graph")
    public java.util.Map<String, Object> getGraph() {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        java.util.List<java.util.Map<String, Object>> nodes = new java.util.ArrayList<>();
        for (org.example.cargameFx.graph.Node n : this.graph.getNodes()) {
            nodes.add(java.util.Map.of("id", n.getId(), "x", n.getX(), "y", n.getY(), "type", n.getType().toString()));
        }
        java.util.List<java.util.Map<String, Object>> edges = new java.util.ArrayList<>();
        for (org.example.cargameFx.graph.Edge e : this.graph.getEdges()) {
            edges.add(java.util.Map.of("source", e.getFrom().getId(), "target", e.getTo().getId()));
        }
        result.put("nodes", nodes);
        result.put("edges", edges);
        return result;
    }

}
