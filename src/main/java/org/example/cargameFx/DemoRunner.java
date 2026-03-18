package org.example.cargameFx;

import org.example.cargameFx.fxComponents.FXController;
import org.example.cargameFx.observer.ObserverDispatcher;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoRunner implements CommandLineRunner {

    private final Model model;
    private final ObserverDispatcher dispatcher;
    private final CommandQueue commands;

    public DemoRunner(Model model, ObserverDispatcher dispatcher, CommandQueue commands){
        this.model = model;
        this.dispatcher = dispatcher;
        this.commands = commands;
    }

    @Override
    public void run(String @NonNull ... args) throws Exception {
        PhysicsEngine physics = new PhysicsEngine(model);
        GameLoop loop = new GameLoop(physics, commands, dispatcher);
        World world = new World();

        Thread observerThread = new Thread(dispatcher, "ObserverThread");
        observerThread.setDaemon(true);
        observerThread.start();

        Thread loopThread = new Thread(loop, "GameLoopThread");
        loopThread.setDaemon(true);
        loopThread.start();
    }

}
