package org.example.cargameFx;

import org.example.cargameFx.observer.ObserverDispatcher;

import java.util.concurrent.locks.LockSupport;

public class GameLoop implements Runnable {
    private final PhysicsEngine physics;
    private final CommandQueue commands;
    private final ObserverDispatcher dispatcher;

    public GameLoop(PhysicsEngine physics, CommandQueue commands, ObserverDispatcher dispatcher){
        this.physics = physics;
        this.commands = commands;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        long tickDelayMs = 16;
        while (!Thread.currentThread().isInterrupted()) {
            long start = System.currentTimeMillis();

            commands.executeAll();
            physics.update();
            //physics.updateDriveFree();
            dispatcher.dispatch(physics::notifyObservers);

            long elapsed = System.currentTimeMillis() - start;
            long sleepTime = tickDelayMs - elapsed;
            if (sleepTime > 0) {
                LockSupport.parkNanos(sleepTime * 1_000_000);
            }else {
                Thread.yield();
            }
        }
    }
}
