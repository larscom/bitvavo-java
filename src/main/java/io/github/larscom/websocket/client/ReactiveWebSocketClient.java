package io.github.larscom.websocket.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.internal.ObjectMapperProvider;
import io.github.larscom.util.Either;
import io.github.larscom.websocket.*;
import io.github.larscom.websocket.Error;
import io.github.larscom.websocket.subscription.Subscription;
import io.github.larscom.websocket.subscription.SubscriptionWithInterval;
import io.github.larscom.websocket.subscription.SubscriptionWithMarkets;
import io.github.larscom.websocket.subscription.SubscriptionValue;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class ReactiveWebSocketClient {
    private boolean running = false;
    private WebSocket webSocket;

    private final PublishSubject<Either<MessageIn, io.github.larscom.websocket.Error>> messagePublisher;

    public ReactiveWebSocketClient() throws InterruptedException {
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
        final var activeSubscriptions = new HashMap<ChannelName, SubscriptionValue>();

        Thread.startVirtualThread(() -> {
            while (running) {
                try {
                    webSocket = new WebSocket(ObjectMapperProvider.getObjectMapper());
                    webSocket.setConnectionLostTimeout(5);
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

    private static List<Channel> mapToChannels(final HashMap<ChannelName, SubscriptionValue> subscriptions) {
        return subscriptions
            .entrySet()
            .stream()
            .map(entry -> {
                final var channelBuilder = Channel.builder()
                    .name(entry.getKey());

                if (entry.getValue() instanceof final SubscriptionWithMarkets value) {
                    channelBuilder.markets(value.getMarkets());
                }

                if (entry.getValue() instanceof final SubscriptionWithInterval value) {
                    final var intervals = value.getIntervalWithMarkets().keySet();
                    final var markets = value.getIntervalWithMarkets()
                        .values()
                        .stream()
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet());

                    channelBuilder.markets(markets).intervals(intervals);
                }

                return channelBuilder.build();
            })
            .toList();
    }
}
