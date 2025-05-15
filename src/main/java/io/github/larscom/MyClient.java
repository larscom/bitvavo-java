package io.github.larscom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MyClient extends WebSocketClient {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public MyClient(final URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(final ServerHandshake handshakedata) {
        System.out.println("opened connection");
        send("Hello, it is me. Mario :)");
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

