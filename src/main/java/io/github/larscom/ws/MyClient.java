package io.github.larscom.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MyClient extends WebSocketClient {
    private final ObjectMapper objectMapper;
    private final PublishSubject<String> subject;

    public MyClient(final URI serverURI, final ObjectMapper objectMapper) {
        super(serverURI);
        this.objectMapper = objectMapper;
        this.subject = PublishSubject.create();
    }

    public Observable<String> getObservable() {
        return Observable.wrap(this.subject);
    }

    public void send(final MessageOut message) throws JsonProcessingException {
        send(objectMapper.writeValueAsString(message));
    }

    @Override
    public void onOpen(final ServerHandshake handshakedata) {
    }

    @Override
    public void onMessage(final String message) {
        System.out.println("received: " + message);

        try {
            final var messageIn = objectMapper.readValue(message, MessageIn.class);
            System.out.println("received: " + messageIn);
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
}

