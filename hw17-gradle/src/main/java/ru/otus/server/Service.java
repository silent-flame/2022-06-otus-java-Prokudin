package ru.otus.server;

import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.otus.protobuf.generated.RemoteServiceGrpc;
import ru.otus.protobuf.generated.ResponseValue;
import ru.otus.protobuf.generated.ValueRequest;

@Slf4j
public class Service extends RemoteServiceGrpc.RemoteServiceImplBase {
    @Override
    public void streamValues(ValueRequest request, StreamObserver<ResponseValue> responseObserver) {
        for (long value = request.getFirstValue(); value <= request.getLastValue(); value++) {
            responseObserver.onNext(ResponseValue.newBuilder()
                    .setValue(value)
                    .build());
            sleep(2_000);
            log.info("Value sent = {}", value);
        }
        responseObserver.onCompleted();
    }

    @SneakyThrows
    private static void sleep(long millis) {
        Thread.sleep(millis);
    }
}