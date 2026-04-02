package org.example.cargame.observer;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class ObserverDispatcher implements Runnable{

    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    public void dispatch(Runnable task){
        queue.add(task);
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            try{
                Runnable task = queue.take();
                try {
                    task.run();
                } catch (Exception e) {
                    System.err.println("Exception in ObserverThread task: " + e.getMessage());
                }
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }
}
