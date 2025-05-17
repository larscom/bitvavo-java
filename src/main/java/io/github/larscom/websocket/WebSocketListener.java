package io.github.larscom.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import io.github.larscom.Jackson;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WebSocketListener {
    private boolean running = false;
    private WebSocket webSocket;

    private final PublishSubject<Either<MessageIn, Error>> messagePublisher;

    public WebSocketListener() throws InterruptedException {
        this.messagePublisher = PublishSubject.create();

        startBlocking();
    }

    public Flowable<Either<MessageIn, Error>> stream() {
        return messagePublisher.toFlowable(BackpressureStrategy.BUFFER);
    }

    public void subscribe(final List<Channel> channels) throws JsonProcessingException {
        if (running) {
            final var message = MessageOut.builder()
                .action(Action.SUBSCRIBE)
                .channels(channels)
                .build();

            webSocket.send(message);
        } else {
            throw new IllegalStateException("WebSocket thread is not running");
        }
    }

    public void unsubscribe(final List<Channel> channels) throws JsonProcessingException {
        if (running) {
            final var message = MessageOut.builder()
                .action(Action.UNSUBSCRIBE)
                .channels(channels)
                .build();

            webSocket.send(message);
        } else {
            throw new IllegalStateException("WebSocket thread is not running");
        }
    }

    public void close() {
        running = false;
        webSocket.terminate();
    }

    private void startBlocking() throws InterruptedException {
        running = true;

        final var startLatch = new CountDownLatch(1);
        final var activeSubscriptions = new HashMap<ChannelName, List<String>>();

        Thread.startVirtualThread(() -> {
            while (running) {
                try {
                    webSocket = new WebSocket(Jackson.getObjectMapper());
                    if (webSocket.connectBlocking()) {
                        startLatch.countDown();

                        if (!activeSubscriptions.isEmpty()) {
                            final var message = MessageOut.builder()
                                .action(Action.SUBSCRIBE)
                                .channels(mapToChannels(activeSubscriptions))
                                .build();

                            webSocket.send(message);
                        }

                        webSocket.stream().subscribe(either -> {
                            if (either.isLeft() && either.getLeft() instanceof final Subscription subscription) {
                                activeSubscriptions.clear();
                                activeSubscriptions.putAll(subscription.getActiveSubscriptions());
                            }
                            messagePublisher.onNext(either);
                        });

                        webSocket.blockUntilClosed();
                    } else {
                        Thread.sleep(2000);
                    }
                } catch (final InterruptedException | URISyntaxException | JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        startLatch.await();
    }

    private static List<Channel> mapToChannels(final HashMap<ChannelName, List<String>> subscriptions) {
        return subscriptions
            .entrySet()
            .stream()
            .map(entry -> Channel.builder()
                .name(entry.getKey())
                .markets(entry.getValue())
                .build())
            .toList();
    }
}
