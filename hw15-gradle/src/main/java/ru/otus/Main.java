package ru.otus;

import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        var workerCounter = new AtomicInteger(1);
        var thread1 = new Thread(new Worker(1, workerCounter, 2), "Thread-1");
        var thread2 = new Thread(new Worker(2, workerCounter, 2), "Thread-2");
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }

    @SneakyThrows
    public static void main2(String[] args) {
        var workerCounter = new AtomicInteger(1);
        var thread1 = new Thread(new Worker(1, workerCounter, 4), "Thread-1");
        var thread2 = new Thread(new Worker(2, workerCounter, 4), "Thread-2");
        var thread3 = new Thread(new Worker(3, workerCounter, 4), "Thread-3");
        var thread4 = new Thread(new Worker(4, workerCounter, 4), "Thread-4");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
    }
}