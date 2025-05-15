package io.github.larscom.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MyClient extends WebSocketClient {
    private final ObjectMapper objectMapper;
    private final PublishSubject<MessageIn> messagePublisher;
    private final PublishSubject<BitvavoError> errorPublisher;
    private final HashMap<MessageInEvent, Class<? extends MessageIn>> eventMap = new HashMap<>(
        Map.of(MessageInEvent.TICKER, Ticker.class)
    );

    public MyClient(final URI serverURI, final ObjectMapper objectMapper) {
        super(serverURI);
        this.objectMapper = objectMapper;
        this.messagePublisher = PublishSubject.create();
        this.errorPublisher = PublishSubject.create();
    }

    public Observable<MessageIn> getMessage() {
        return Observable.wrap(messagePublisher);
    }

    public Observable<BitvavoError> getError() {
        return Observable.wrap(errorPublisher);
    }

    public void send(final MessageOut message) throws JsonProcessingException {
//        send(objectMapper.writeValueAsString(message));
        send("test");
    }

    @Override
    public void onOpen(final ServerHandshake handshakedata) {
    }

    @Override
    public void onMessage(final String message) {
        System.out.println("received: " + message);

        try {
            final JsonNode json = objectMapper.readTree(message);
            final var maybeEvent = Optional.ofNullable(json.get("event")).map(JsonNode::asText).map(MessageInEvent::deserialize);
//            final var maybeError = Optional.ofNullable(json.get("error")).map(JsonNode::asText).flatMap(text -> tryDeserialize(text, BitvavoError.class));
            final var maybeError = Optional.ofNullable(json.get("error")).flatMap(text -> tryDeserialize(message, BitvavoError.class));

            maybeEvent.flatMap(event -> tryDeserialize(message, eventMap.get(event))).ifPresent(messagePublisher::onNext);
            maybeError.ifPresent(errorPublisher::onNext);

        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onClose(final int code, final String reason, final boolean remote) {

    }

    @Override
    public void onError(final Exception ex) {
    }

    private <T> Optional<T> tryDeserialize(final String json, final Class<T> clazz) {
        try {
            return Optional.of(objectMapper.readValue(json, clazz));
        } catch (final JsonProcessingException e) {
            return Optional.empty();
        }
    }
}

