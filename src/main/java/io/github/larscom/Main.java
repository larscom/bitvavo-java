package io.github.larscom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import io.github.larscom.ws.*;

import java.net.URISyntaxException;
import java.util.List;

public class Main {

    public static void main(final String[] args) {
        final var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());

        while (true) {
            try {
                final var socket = new WebSocket(objectMapper);
                if (socket.connectBlocking()) {
                    socket.stream()
                        .filter(Either::isLeft)
                        .map(Either::getLeft)
                        .subscribe(message -> {
                            switch (message) {
                                case final Ticker ticker -> {
                                    System.out.println("Ticker: " + ticker);
                                }
                                case final Subscription subscription -> {
                                    System.out.println("Subscribed: " + subscription);
                                }
                                default -> throw new IllegalStateException("Unexpected value: " + message);
                            }
                        });

                    final var message = MessageOut.builder()
                        .action(Action.SUBSCRIBE)
                        .channels(List.of(Channel.builder()
                            .name("ticker")
                            .markets(List.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR"))
                            .build()))
                        .build();

                    socket.send(message);

                    socket.blockUntilClosed();
                } else {
                    Thread.sleep(1000);
                }
            } catch (final InterruptedException | URISyntaxException | JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}