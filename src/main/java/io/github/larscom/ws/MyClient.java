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

public class MyClient extends WebSocketClient {
    private final ObjectMapper objectMapper;
    private final PublishSubject<MessageIn> messagePublisher;
    private final PublishSubject<BitvavoError> errorPublisher;

    private final HashMap<MessageInEvent, Class<? extends MessageIn>> eventMap = new HashMap<>() {{
        put(MessageInEvent.TICKER, Ticker.class);
        put(MessageInEvent.SUBSCRIBED, Subscription.class);
        put(MessageInEvent.UNSUBSCRIBED, Subscription.class);
    }};

    public MyClient(final URI uri, final ObjectMapper objectMapper) throws InterruptedException {
        super(uri);
        this.objectMapper = objectMapper;
        this.messagePublisher = PublishSubject.create();
        this.errorPublisher = PublishSubject.create();

        connectBlocking();
    }

    public Flowable<MessageIn> stream() {
        return messagePublisher.toFlowable(BackpressureStrategy.BUFFER);
    }

    public Flowable<BitvavoError> error() {
        return errorPublisher.toFlowable(BackpressureStrategy.LATEST);
    }

    public void send(final MessageOut message) throws JsonProcessingException {
        send(objectMapper.writeValueAsString(message));
    }

    @Override
    public void onOpen(final ServerHandshake handshakedata) {
    }

    @Override
    public void onMessage(final String message) {
        System.out.println("Incoming message: " + message);
        try {
            final JsonNode json = objectMapper.readTree(message);

            final var maybeMessage = Optional.ofNullable(json.get("event"))
                .map(JsonNode::asText)
                .map(MessageInEvent::deserialize)
                .flatMap(event -> tryDeserialize(message, eventMap.get(event)));

            maybeMessage.ifPresent(messagePublisher::onNext);

            final var maybeError = Optional.ofNullable(json.get("error"))
                .flatMap(__ -> tryDeserialize(message, BitvavoError.class));

            maybeError.ifPresent(errorPublisher::onNext);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClose(final int code, final String reason, final boolean remote) {
        System.out.println("Connection closed: " + code + " " + reason);
    }

    @Override
    public void onError(final Exception ex) {
        System.out.println("Error!!!!!!: " + ex.getMessage());
    }

    private <T> Optional<T> tryDeserialize(final String json, final Class<T> clazz) {
        try {
            return Optional.of(objectMapper.readValue(json, clazz));
        } catch (final JsonProcessingException e) {
            return Optional.empty();
        }
    }
}

