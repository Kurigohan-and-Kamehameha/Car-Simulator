package org.example.cargameFx;

import org.example.cargameFx.entity.EntityId;
import org.example.cargameFx.enums.EngineType;
import org.example.cargameFx.enums.State;
import org.example.cargameFx.observer.ObserverDispatcher;
import org.example.cargameFx.snapshot.VelocitySnapshot;
import org.springframework.stereotype.Service;

@Service
public class Servicelayer {

    private final Model model;
    private final CommandQueue commands;
    private final ObserverDispatcher dispatcher;

    public Servicelayer(Model model, CommandQueue commands, ObserverDispatcher dispatcher) {
        this.commands = commands;
        this.dispatcher = dispatcher;
        this.model = model;
    }

    public void stop(){
        EntityId playerId = model.getPlayerId();
        commands.submit(() -> {
            model.getStates().get(playerId).state = State.WAIT_AT_WORKSHOP;

            dispatcher.dispatch(() ->
                    model.getStates().get(playerId).notifyObservers()
            );
        });
    }

    public void setDirection(int dx, int dy){
        EntityId playerId = model.getPlayerId();
        commands.submit(() -> {
            double speed = model.getSpeeds().get(playerId).getSpeed();
            VelocitySnapshot newSnap = new VelocitySnapshot(dx * speed, dy * speed);
            model.getVelocities().get(playerId).setSnapshot(newSnap);
            model.getStates().get(playerId).state = State.DRIVE;

            dispatcher.dispatch(() ->
                    model.getStates().get(playerId).notifyObservers()
            );
        });
    }

    public void setColor(String color, Runnable afterUpdate){
        EntityId playerId = model.getPlayerId();
        commands.submit(() -> {
            if (State.WAIT_AT_WORKSHOP == model.getStates().get(playerId).state) {
                model.getColors().get(playerId).setColor(color);

                dispatcher.dispatch(() -> {
                    model.getColors().get(playerId).notifyObservers();
                    if(afterUpdate != null){
                        afterUpdate.run();
                    }
                });
            }
        });
    }

    public void setEngine(EngineType engineType, Runnable afterUpdate){
        EntityId playerId = model.getPlayerId();
        commands.submit(() -> {
            if(State.WAIT_AT_WORKSHOP == model.getStates().get(playerId).state) {
                model.getEngines().get(playerId).setEngine(engineType);

                dispatcher.dispatch(() -> {
                    model.getEngines().get(playerId).notifyObservers();
                    if(afterUpdate != null){
                        afterUpdate.run();
                    }
                });
            }
        });
    }

}
