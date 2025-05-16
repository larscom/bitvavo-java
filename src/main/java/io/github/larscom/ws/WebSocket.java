package io.github.larscom.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class WebSocket extends WebSocketClient {
    private final ObjectMapper objectMapper;
    private final PublishSubject<Either<MessageIn, BitvavoError>> messagePublisher;

    private final CountDownLatch closeLatch = new CountDownLatch(1);
    private final HashMap<MessageInEvent, Class<? extends MessageIn>> eventMap = new HashMap<>() {{
        put(MessageInEvent.TICKER, Ticker.class);
        put(MessageInEvent.SUBSCRIBED, Subscription.class);
        put(MessageInEvent.UNSUBSCRIBED, Subscription.class);
    }};

    public WebSocket(final URI uri, final ObjectMapper objectMapper) throws InterruptedException {
        super(uri);
        this.objectMapper = objectMapper;
        this.messagePublisher = PublishSubject.create();
    }

    public Flowable<Either<MessageIn, BitvavoError>> stream() {
        return messagePublisher.toFlowable(BackpressureStrategy.BUFFER);
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

    @Override
    public void onMessage(final String message) {
        System.out.println("Incoming message: " + message);
        try {
            final JsonNode json = objectMapper.readTree(message);

            final var maybeMessage = Optional.ofNullable(json.get("event"))
                .map(JsonNode::asText)
                .map(MessageInEvent::deserialize)
                .flatMap(event -> tryDeserialize(message, eventMap.get(event)))
                .map(Either::<MessageIn, BitvavoError>left);

            final var maybeError = Optional.ofNullable(json.get("error"))
                .flatMap(__ -> tryDeserialize(message, BitvavoError.class))
                .map(Either::<MessageIn, BitvavoError>right);

            final var either = maybeMessage.or(() -> maybeError);
            either.ifPresent(messagePublisher::onNext);

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
    }

    @Override
    public void onError(final Exception e) {
    }

    private <T> Optional<T> tryDeserialize(final String json, final Class<T> clazz) {
        try {
            return Optional.of(objectMapper.readValue(json, clazz));
        } catch (final JsonProcessingException e) {
            return Optional.empty();
        }
    }
}

