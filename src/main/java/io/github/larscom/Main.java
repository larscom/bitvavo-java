package io.github.larscom;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.websocket.*;

import java.util.List;

public class Main {

    public static void main(final String[] args) throws InterruptedException, JsonProcessingException {
        final var listener = new WebSocketListener();

        final var channels = List.of(
            Channel.builder().name(ChannelName.TICKER).markets(List.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build(),
            Channel.builder().name(ChannelName.BOOK).markets(List.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build()
        );

        listener.subscribe(channels);

        listener.stream().filter(Either::isRight).map(Either::getRight).subscribe(System.out::println);

        listener.stream()
            .filter(Either::isLeft)
            .map(Either::getLeft)
            .subscribe(message -> {
                switch (message) {
                    case final Ticker ticker -> {
                        System.out.println("Ticker: " + ticker);
                    }
                    case final Book book -> {
                        System.out.println("Book: " + book);
                    }
                    default -> {
                        System.out.println("Unhandled type: " + message.getClass().getSimpleName());
                    }
                }
            });

        Thread.currentThread().join();
    }
}
