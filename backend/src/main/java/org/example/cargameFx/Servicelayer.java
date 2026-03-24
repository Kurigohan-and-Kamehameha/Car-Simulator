package org.example.cargameFx;

import org.example.cargameFx.commands.SetDirectionCommand;
import org.example.cargameFx.engine.Engine;
import org.example.cargameFx.entity.EntityId;
import org.example.cargameFx.enums.EngineType;
import org.example.cargameFx.enums.State;
import org.example.cargameFx.graph.Graph;
import org.example.cargameFx.observer.ObserverDispatcher;
import org.example.cargameFx.snapshot.EnergyStorageSnapshot;
import org.example.cargameFx.snapshot.SpeedSnapshot;
import org.springframework.stereotype.Service;

@Service
public class Servicelayer {

    private final Model model;
    private final CommandQueue commands;
    private final ObserverDispatcher dispatcher;
    private final Dijkstra dij;
    private final Graph graph;

    public Servicelayer(Model model, CommandQueue commands, ObserverDispatcher dispatcher, Dijkstra dij, Graph graph) {
        this.commands = commands;
        this.dispatcher = dispatcher;
        this.model = model;
        this.dij = dij;
        this.graph = graph;
    }

    public void stop() {
        EntityId playerId = model.getPlayerId();
        commands.submit(() -> {
            model.getStates().get(playerId).set(State.WAIT_AT_WORKSHOP);

            dispatcher.dispatch(() -> model.getStates().get(playerId).notifyObservers());
        });
    }

    public void setDirection(String targetId) {
        EntityId playerId = model.getPlayerId();

        commands.submit(new SetDirectionCommand(
                model, dij, graph, dispatcher, playerId, targetId));
    }

    public void setColor(String color) {
        EntityId playerId = model.getPlayerId();
        commands.submit(() -> {
            if (State.WAIT_AT_WORKSHOP == model.getStates().get(playerId).get()) {
                model.getColors().get(playerId).setColor(color);

                dispatcher.dispatch(() -> {
                    model.getColors().get(playerId).notifyObservers();
                });
            }
        });
    }

    public void setSpeed(double speed) {
        EntityId playerId = model.getPlayerId();
        commands.submit(() -> {
            model.getSpeeds().get(playerId).setSnapshot(new SpeedSnapshot(speed));
        });
    }

    public void setEngine(EngineType engineType) {
        EntityId playerId = model.getPlayerId();
        commands.submit(() -> {
            if (State.WAIT_AT_WORKSHOP == model.getStates().get(playerId).get()) {
                model.getEngines().get(playerId).setEngine(engineType);
                Engine engine = model.getEngines().get(playerId).getActiveEngine();
                model.getStorage().get(playerId)
                        .setSnapshot(new EnergyStorageSnapshot(engine.capacity(), engine.capacity()));

                dispatcher.dispatch(() -> {
                    model.getEngines().get(playerId).notifyObservers();
                });
            }
        });
    }

}
