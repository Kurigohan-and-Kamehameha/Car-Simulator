package org.example.cargame;

import org.example.cargame.observer.GameStateView;
import org.example.cargame.observer.ObserverDispatcher;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoRunner implements CommandLineRunner {

    private final CarModel model;
    private final ObserverDispatcher dispatcher;
    private final CommandQueue commands;
    private final GameStateView gameStateView;

    public DemoRunner(CarModel model, ObserverDispatcher dispatcher, CommandQueue commands,
            GameStateView gameStateView) {
        this.model = model;
        this.dispatcher = dispatcher;
        this.commands = commands;
        this.gameStateView = gameStateView;
    }

    @Override
    public void run(String @NonNull... args) {
        PhysicsEngine physics = new PhysicsEngine(model, gameStateView, dispatcher);
        GameLoop loop = new GameLoop(physics, commands);

        Thread.ofVirtual().name("ObserverThread").start(dispatcher);
        Thread.ofVirtual().name("GameLoopThread").start(loop);
    }
}
