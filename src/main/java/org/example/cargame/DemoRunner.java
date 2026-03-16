package org.example.cargame;

import org.example.cargame.controller.FXController;
import org.example.cargame.entity.Car;
import org.example.cargame.enums.EngineType;
import org.example.cargame.observer.ObserverDispatcher;
import org.example.cargame.subject.Model;
import org.example.cargame.observer.ColorEngineView;
import org.example.cargame.observer.PossitionView;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class DemoRunner implements CommandLineRunner {

    private final Model model;
    private final Servicelayer servicelayer;
    private final ObserverDispatcher dispatcher;
    private final CommandQueue commands;

    public DemoRunner(Model model, Servicelayer servicelayer, ObserverDispatcher dispatcher, CommandQueue commands){
        this.model = model;
        this.servicelayer = servicelayer;
        this.dispatcher = dispatcher;
        this.commands = commands;
    }

    @Override
    public void run(String @NonNull ... args) throws Exception {
        PhysicsEngine physics = new PhysicsEngine(model);
        GameLoop loop = new GameLoop(physics, commands, dispatcher);
        ColorEngineView viewColEng = new ColorEngineView(model);
        PossitionView viewPos = new PossitionView(model);

        Thread observerThread = new Thread(dispatcher, "ObserverThread");
        observerThread.setDaemon(true);
        observerThread.start();

        Thread loopThread = new Thread(loop, "GameLoopThread");
        loopThread.setDaemon(true);
        loopThread.start();

        Car car = (Car) model.getEntityList().getFirst();
        FXController fxController = new FXController(servicelayer);

        fxController.setColor("blue");
        fxController.setEngine(EngineType.ELECTRIC);

    }
}
