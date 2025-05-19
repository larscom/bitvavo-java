package io.github.larscom.websocket.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.internal.CryptoUtils;
import io.github.larscom.internal.Either;
import io.github.larscom.internal.ObjectMapperProvider;
import io.github.larscom.websocket.*;
import io.github.larscom.websocket.Error;
import io.github.larscom.websocket.account.Authentication;
import io.github.larscom.websocket.account.Credentials;
import io.github.larscom.websocket.account.Order;
import io.github.larscom.websocket.book.Book;
import io.github.larscom.websocket.candle.Candle;
import io.github.larscom.websocket.subscription.Subscription;
import io.github.larscom.websocket.subscription.SubscriptionValue;
import io.github.larscom.websocket.subscription.SubscriptionWithInterval;
import io.github.larscom.websocket.subscription.SubscriptionWithMarkets;
import io.github.larscom.websocket.ticker.Ticker;
import io.github.larscom.websocket.ticker.Ticker24h;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class ReactiveWebSocketClient {
    private boolean running = false;
    private WebSocket webSocket;

    private final BehaviorSubject<Either<MessageIn, Error>> incoming;
    private final Flowable<Either<MessageIn, Error>> outgoing;

    public ReactiveWebSocketClient() throws InterruptedException {
        this(Optional.empty());
    }

    public ReactiveWebSocketClient(@NonNull final Credentials credentials) throws InterruptedException {
        this(Optional.of(credentials));
    }

    private ReactiveWebSocketClient(final Optional<Credentials> credentials) throws InterruptedException {
        this.incoming = BehaviorSubject.create();
        this.outgoing = incoming.toFlowable(BackpressureStrategy.BUFFER);

        startBlocking(credentials);
    }

    public Flowable<MessageIn> messages() {
        return outgoing.filter(Either::isLeft).map(Either::getLeft);
    }

    public Flowable<Ticker> ticker() {
        return mapTo(messages(), Ticker.class);
    }

    public Flowable<Ticker24h> ticker24h() {
        return mapTo(messages(), Ticker24h.class);
    }

    public Flowable<Book> book() {
        return mapTo(messages(), Book.class);
    }

    public Flowable<Subscription> subscription() {
        return mapTo(messages(), Subscription.class);
    }

    public Flowable<Candle> candles() {
        return mapTo(messages(), Candle.class);
    }

    public Flowable<Trade> trades() {
        return mapTo(messages(), Trade.class);
    }

    public Flowable<Order> orders() {
        return mapTo(messages(), Order.class);
    }

    public Flowable<Error> error() {
        return outgoing.filter(Either::isRight).map(Either::getRight);
    }

    public void subscribe(final Set<Channel> channels) throws JsonProcessingException {
        if (running) {
            sendSubscribe(channels);
        } else {
            throw new IllegalStateException("WebSocket thread is not running");
        }
    }

    public void unsubscribe(final Set<Channel> channels) throws JsonProcessingException {
        if (running) {
            sendUnsubscribe(channels);
        } else {
            throw new IllegalStateException("WebSocket thread is not running");
        }
    }

    public void close() {
        running = false;
        webSocket.terminate();
    }

    private void startBlocking(final Optional<Credentials> credentials) throws InterruptedException {
        running = true;

        final var startLatch = new CountDownLatch(1);
        final var activeSubscriptions = new HashMap<ChannelName, SubscriptionValue>();

        Thread.startVirtualThread(() -> {
            while (running) {
                try {
                    webSocket = new WebSocket(ObjectMapperProvider.getObjectMapper());
                    if (webSocket.connectBlocking()) {
                        if (credentials.isPresent()) {
                            sendAuthenticate(credentials.get());
                        } else {
                            startLatch.countDown();

                            if (!activeSubscriptions.isEmpty()) {
                                sendSubscribe(mapToChannels(activeSubscriptions));
                            }
                        }

                        webSocket.stream().subscribe(either -> {
                            if (either.isLeft()) {
                                if (either.getLeft() instanceof final Subscription subscription) {
                                    activeSubscriptions.clear();
                                    activeSubscriptions.putAll(subscription.getActiveSubscriptions());
                                }
                                if (either.getLeft() instanceof final Authentication authentication) {
                                    startLatch.countDown();

                                    if (authentication.getAuthenticated() && !activeSubscriptions.isEmpty()) {
                                        sendSubscribe(mapToChannels(activeSubscriptions));
                                    }
                                }
                            }

                            if (either.isRight() && credentials.isPresent()) {
                                startLatch.countDown();
                            }

                            incoming.onNext(either);
                        });

                        webSocket.blockUntilClosed();
                    } else {
                        Thread.sleep(2000);
                    }
                } catch (final InterruptedException | URISyntaxException | JsonProcessingException |
                               NoSuchAlgorithmException | InvalidKeyException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        startLatch.await();
    }

    private void sendSubscribe(final Set<Channel> channels) throws JsonProcessingException {
        final var message = MessageOut.builder()
            .action(Action.SUBSCRIBE)
            .channels(channels)
            .build();

        webSocket.send(message);
    }

    private void sendUnsubscribe(final Set<Channel> channels) throws JsonProcessingException {
        final var message = MessageOut.builder()
            .action(Action.UNSUBSCRIBE)
            .channels(channels)
            .build();

        webSocket.send(message);
    }

    private void sendAuthenticate(@NonNull final Credentials credentials) throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        final var timestamp = Instant.now().toEpochMilli();
        final var message = MessageOut.builder()
            .action(Action.AUTHENTICATE)
            .key(credentials.apiKey())
            .signature(CryptoUtils.createSignature(
                "GET",
                "/websocket",
                Optional.empty(),
                timestamp,
                credentials.apiSecret()
            ))
            .timestamp(timestamp)
            .build();

        webSocket.send(message);
    }

    private static <T> Flowable<T> mapTo(
        final Flowable<MessageIn> source,
        final Class<T> clazz
    ) {
        return source
            .filter(clazz::isInstance)
            .map(clazz::cast);
    }

    private static Set<Channel> mapToChannels(final HashMap<ChannelName, SubscriptionValue> subscriptions) {
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
            .collect(Collectors.toSet());
    }
}
