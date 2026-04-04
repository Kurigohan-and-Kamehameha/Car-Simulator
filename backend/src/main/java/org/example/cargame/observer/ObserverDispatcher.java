package org.example.cargame.observer;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

@Component
public class ObserverDispatcher implements Runnable {

    private static final Logger log = Logger.getLogger(ObserverDispatcher.class.getName());
    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(2000);

    public void dispatch(Runnable task) {
        if (!queue.offer(task)) {
            log.warning("ObserverDispatcher queue is full! Dropping frontend frame update to prevent memory leak.");
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Runnable task = queue.take();
                try {
                    task.run();
                } catch (Exception e) {
                    System.err.println("Exception in ObserverThread task: " + e.getMessage());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
