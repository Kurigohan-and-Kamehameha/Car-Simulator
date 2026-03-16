package org.example.cargameFx;

import org.example.cargameFx.controller.FXController;
import org.example.cargameFx.entity.Car;
import org.example.cargameFx.enums.EngineType;
import org.example.cargameFx.observer.ObserverDispatcher;
import org.example.cargameFx.subject.Model;
import org.example.cargameFx.observer.ColorEngineView;
import org.example.cargameFx.observer.PossitionView;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
