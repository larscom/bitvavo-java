package io.github.larscom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Main {
    public static void main(final String[] args) throws URISyntaxException, InterruptedException, JsonProcessingException {

        final var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());

        final var c = new MyClient(new URI("wss://ws.bitvavo.com/v2"), objectMapper);
        c.getObservable().subscribe(s -> {
            System.out.println("Received obs: " + s);
        });

        c.connectBlocking();

        final var message = MessageOut.builder()
            .action(Action.SUBSCRIBE)
            .channels(List.of(Channel.builder()
                .name("ticker")
                .markets(List.of("ETH-EUR"))
                .build()))
            .build();

        c.send(message);

        Thread.sleep(50000);
    }
}