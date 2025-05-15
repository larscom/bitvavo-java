package io.github.larscom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import io.github.larscom.ws.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Main {
    public static void main(final String[] args) throws URISyntaxException, InterruptedException, JsonProcessingException {

        final var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());

        final var client = new MyClient(new URI("wss://ws.bitvavo.com/v2"), objectMapper);
        client.stream().subscribe(messageIn -> {
            switch (messageIn) {
                case final Ticker ticker -> {
                    System.out.println("Ticker: " + ticker);
                }
                case final Subscription subscription -> {
                    System.out.println("Subscribed: " + subscription);
                }
                default -> throw new IllegalStateException("Unexpected value: " + messageIn);
            }
        });

        client.error().subscribe(e -> {
            System.out.println("Error: " + e);
        });


        final var message = MessageOut.builder()
            .action(Action.SUBSCRIBE)
            .channels(List.of(Channel.builder()
                .name("ticker")
                .markets(List.of("ETH-EUR"))
                .build()))
            .build();

        client.send(message);

        Thread.currentThread().join();
    }
}