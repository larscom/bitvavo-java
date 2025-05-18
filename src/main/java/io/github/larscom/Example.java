package io.github.larscom;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.websocket.*;
import io.github.larscom.websocket.subscription.Subscription;

import java.util.List;
import java.util.Set;

class Example {

    public static void main(final String[] args) throws InterruptedException, JsonProcessingException {
        final var client = new ReactiveWebSocketClient();

        final var channels = List.of(
            Channel.builder().name(ChannelName.TICKER).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build(),
            Channel.builder().name(ChannelName.BOOK).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build(),
            Channel.builder().name(ChannelName.CANDLES).intervals(Set.of(Interval.H8, Interval.H2)).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build()
        );

        client.subscribe(channels);

        client.stream().filter(Either::isRight).map(Either::getRight).subscribe(System.out::println);

        client.stream()
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
                    case final Subscription subscription -> {
                        System.out.println("Subscription: " + subscription.getActiveSubscriptions());
                    }
                    default -> {
                        System.out.println("Unhandled type: " + message.getClass().getSimpleName());
                    }
                }
            });

        Thread.currentThread().join();
    }
}
