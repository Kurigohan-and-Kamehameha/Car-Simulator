package org.example.cargameFx;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class CommandQueue {

    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    public void submit(Runnable command) {
        queue.add(command);
    }

    public void executeAll() {

        Runnable command;

        while ((command = queue.poll()) != null) {
            command.run();
        }

    }
}
