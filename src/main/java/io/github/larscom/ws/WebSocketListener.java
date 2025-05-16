package io.github.larscom.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.net.URISyntaxException;
import java.util.List;

public class WebSocketListener {
    private boolean running = false;
    private WebSocket webSocket;

    private final ObjectMapper objectMapper;

    public WebSocketListener() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
    }

    public void subscribe() throws JsonProcessingException {
        final var message = MessageOut.builder()
            .action(Action.SUBSCRIBE)
            .channels(List.of(Channel.builder()
                .name("ticker")
                .markets(List.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR"))
                .build()))
            .build();

        webSocket.send(message);
    }

    public void start() {
        if (running) {
            return;
        }

        running = true;

        new Thread(() -> {
            while (running) {
                try {
                    webSocket = new WebSocket(objectMapper);
                    if (webSocket.connectBlocking()) {
                        webSocket.blockUntilClosed();
                    } else {
                        Thread.sleep(2000);
                    }
                } catch (final InterruptedException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
