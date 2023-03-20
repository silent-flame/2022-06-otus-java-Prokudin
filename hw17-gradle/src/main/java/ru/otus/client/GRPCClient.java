package ru.otus.client;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.otus.protobuf.generated.RemoteServiceGrpc;
import ru.otus.protobuf.generated.ResponseValue;
import ru.otus.protobuf.generated.ValueRequest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class GRPCClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    @SneakyThrows
    public static void main(String[] args) {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT).usePlaintext().build();

        var isNewValue = new AtomicBoolean(false);
        var newValue = new AtomicLong();
        var latch = new CountDownLatch(1);

        var valueObserver = new StreamObserver<ResponseValue>() {
            @Override
            public void onNext(ResponseValue value) {
                synchronized (isNewValue) {
                    isNewValue.set(true);
                    newValue.set(value.getValue());
                    log.info("New value received = {}", value.getValue());
                }
            }

            @Override
            public void onError(Throwable t) {
                log.error("Error", t);
            }

            @Override
            public void onCompleted() {
                log.info("Request processed");
                latch.countDown();
            }
        };

        var stub = RemoteServiceGrpc.newStub(channel);
        stub.streamValues(ValueRequest.newBuilder().setFirstValue(0).setLastValue(30).build(), valueObserver);


        var currentValue = 0L;
        for (int i = 0; i < 50; i++) {
            synchronized (isNewValue) {
                currentValue = currentValue + 1 + (isNewValue.get() ? newValue.get() : 0);
                isNewValue.set(false);
                log.info("currentValue:{}", currentValue);
            }
            sleep(1_000);
        }

        latch.await();
        channel.shutdown();
    }

    @SneakyThrows
    private static void sleep(long millis) {
        Thread.sleep(millis);
    }
}