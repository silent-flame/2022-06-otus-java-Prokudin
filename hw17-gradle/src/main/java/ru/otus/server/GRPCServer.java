package ru.otus.server;

import io.grpc.ServerBuilder;

public class GRPCServer {
    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws Exception {

        var remoteService = new Service();
        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(remoteService).build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}