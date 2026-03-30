package org.example.cargame;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class CommandQueue {

    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(1000);

    public void submit(Runnable command) {
        if (!queue.offer(command)) {
            System.out.println("CommandQueue full! Dropping command.");
        }
    }

    public void executeAll() {
        Runnable command;

        while ((command = queue.poll()) != null) {
            command.run();
        }
    }
}
