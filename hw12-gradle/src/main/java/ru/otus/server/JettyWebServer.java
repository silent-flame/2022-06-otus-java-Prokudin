package ru.otus.server;

public interface JettyWebServer {
    void start() throws Exception;

    void join() throws Exception;

    void stop() throws Exception;
}
