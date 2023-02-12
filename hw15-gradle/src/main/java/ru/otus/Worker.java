package ru.otus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Slf4j
public class Worker implements Runnable {
    private final int number;

    private final AtomicInteger workerCounter;
    private final int totalNumberOfCounters;

    private int counter = 0;

    private static final Object monitor = new Object();

    @Override
    public void run() {
        synchronized (monitor) {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    while (workerCounter.get() != number) {
                        monitor.wait();
                    }
                    log.info("Counter = {}", (++counter));
                    var currentWorkerCounter = workerCounter.get();
                    currentWorkerCounter++;
                    if (currentWorkerCounter > totalNumberOfCounters) {
                        currentWorkerCounter = (currentWorkerCounter % totalNumberOfCounters);
                    }
                    workerCounter.set(currentWorkerCounter);
                    sleep();
                    monitor.notifyAll();
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            log.info(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}