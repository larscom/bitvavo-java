package io.github.larscom.websocket.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.larscom.internal.Either;
import io.github.larscom.websocket.*;
import io.github.larscom.websocket.Error;
import io.github.larscom.websocket.account.Authentication;
import io.github.larscom.websocket.account.Order;
import io.github.larscom.websocket.subscription.Subscription;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

class WebSocket extends WebSocketClient {
    private final ObjectMapper objectMapper;
    private final CountDownLatch closeLatch;
    private final PublishSubject<Either<MessageIn, Error>> incoming;
    private final Flowable<Either<MessageIn, Error>> outgoing;

    private final HashMap<MessageInEvent, Class<? extends MessageIn>> eventMap = new HashMap<>() {{
        put(MessageInEvent.AUTHENTICATE, Authentication.class);
        put(MessageInEvent.SUBSCRIBED, Subscription.class);
        put(MessageInEvent.UNSUBSCRIBED, Subscription.class);
        put(MessageInEvent.TICKER, Ticker.class);
        put(MessageInEvent.TICKER24H, Ticker24h.class);
        put(MessageInEvent.BOOK, Book.class);
        put(MessageInEvent.CANDLE, Candle.class);
        put(MessageInEvent.TRADE, Trade.class);
        put(MessageInEvent.ORDER, Order.class);
    }};

    public WebSocket(final ObjectMapper objectMapper) throws InterruptedException, URISyntaxException {
        super(new URI("wss://ws.bitvavo.com/v2"));
        this.objectMapper = objectMapper;
        this.closeLatch = new CountDownLatch(1);
        this.incoming = PublishSubject.create();
        this.outgoing = incoming.toFlowable(BackpressureStrategy.BUFFER);
    }

    public Flowable<Either<MessageIn, Error>> stream() {
        return outgoing;
    }

    public void send(final MessageOut message) throws JsonProcessingException {
        send(objectMapper.writeValueAsString(message));
    }

    public void blockUntilClosed() throws InterruptedException {
        if (isOpen()) {
            closeLatch.await();
            close();
        }
    }

    public void terminate() {
        if (isOpen()) {
            closeLatch.countDown();
            close();
        }
    }

    @Override
    public void onMessage(final String message) {
        try {
            final JsonNode json = objectMapper.readTree(message);

            final var maybeMessage = Optional.ofNullable(json.get("event"))
                .map(JsonNode::asText)
                .map(MessageInEvent::deserialize)
                .flatMap(event -> maybeDeserialize(message, eventMap.get(event)))
                .map(Either::<MessageIn, Error>left);

            final var maybeError = Optional.ofNullable(json.get("error"))
                .flatMap(__ -> maybeDeserialize(message, Error.class))
                .map(Either::<MessageIn, Error>right);

            final var either = maybeMessage.or(() -> maybeError);
            either.ifPresent(incoming::onNext);

        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClose(final int code, final String reason, final boolean remote) {
        closeLatch.countDown();
    }

    @Override
    public void onOpen(final ServerHandshake serverHandshake) {
        if (serverHandshake.getHttpStatus() != 101) {
            final var error = Error.builder()
                .errorCode(serverHandshake.getHttpStatus())
                .errorMessage(serverHandshake.getHttpStatusMessage())
                .build();

            incoming.onNext(Either.right(error));

            closeLatch.countDown();
        }
    }

    @Override
    public void onError(final Exception e) {
        final var error = Error.builder()
            .errorCode(0)
            .errorMessage(e.getMessage())
            .build();

        incoming.onNext(Either.right(error));
    }

    private <T> Optional<T> maybeDeserialize(final String json, final Class<T> clazz) {
        try {
            return Optional.of(objectMapper.readValue(json, clazz));
        } catch (final JsonProcessingException | RuntimeException e) {
            final var errorMessage = String.format("%s > incoming message: %s", e.getMessage(), json);
            final var error = Error.builder()
                .errorCode(0)
                .errorMessage(errorMessage)
                .build();

            incoming.onNext(Either.right(error));

            return Optional.empty();
        }
    }
}

