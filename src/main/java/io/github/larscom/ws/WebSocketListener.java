package io.github.larscom.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WebSocketListener {
    private boolean running = false;
    private WebSocket webSocket;

    private final CountDownLatch startLatch;
    private final ObjectMapper objectMapper;
    private final PublishSubject<Either<MessageIn, Error>> messagePublisher;

    public WebSocketListener() throws InterruptedException {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());

        this.messagePublisher = PublishSubject.create();
        this.startLatch = new CountDownLatch(1);

        startBlocking();
    }

    public Flowable<Either<MessageIn, Error>> stream() {
        return messagePublisher.toFlowable(BackpressureStrategy.BUFFER);
    }

    public void subscribe() throws JsonProcessingException {
        if (running) {
            final var message = MessageOut.builder()
                .action(Action.SUBSCRIBE)
                .channels(List.of(Channel.builder()
                    .name("ticker")
                    .markets(List.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR"))
                    .build()))
                .build();

            webSocket.send(message);
        }
    }

    public void close() {
        running = false;
        webSocket.terminate();
    }

    private void startBlocking() throws InterruptedException {
        running = true;

        new Thread(() -> {
            while (running) {
                try {
                    webSocket = new WebSocket(objectMapper);
                    if (webSocket.connectBlocking()) {
                        startLatch.countDown();
                        webSocket.stream().subscribe(messagePublisher::onNext);
                        webSocket.blockUntilClosed();
                    } else {
                        Thread.sleep(2000);
                    }
                } catch (final InterruptedException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        startLatch.await();
    }
}
