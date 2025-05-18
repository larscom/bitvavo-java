package io.github.larscom;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.websocket.*;
import io.github.larscom.websocket.client.ReactiveWebSocketClient;
import io.github.larscom.websocket.subscription.Subscription;

import java.util.Set;

class Example {

    public static void main(final String[] args) throws InterruptedException, JsonProcessingException {
        final var client = new ReactiveWebSocketClient();

        final var channels = Set.of(
//            Channel.builder().name(ChannelName.ACCOUNT).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build(),
            Channel.builder().name(ChannelName.TICKER).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build(),
            Channel.builder().name(ChannelName.TICKER24H).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build(),
            Channel.builder().name(ChannelName.BOOK).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build(),
            Channel.builder().name(ChannelName.TRADES).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build(),
            Channel.builder().name(ChannelName.CANDLES).intervals(Set.of(Interval.M1)).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build()
        );

        client.subscribe(channels);

        client.error().subscribe(System.out::println);

        client.ticker().subscribe(System.out::println);
//        client.ticker24h().subscribe(System.out::println);
//        client.book().subscribe(System.out::println);
//        client.subscription().subscribe(System.out::println);
//        client.candles().subscribe(System.out::println);
//        client.trades().subscribe(System.out::println);

//        client.stream()
//            .subscribe(message -> {
//                switch (message) {
//                    case final Ticker ticker -> {
//                        System.out.println("Ticker: " + ticker);
//                    }
//                    case final Book book -> {
//                        System.out.println("Book: " + book);
//                    }
//                    case final Subscription subscription -> {
//                        System.out.println("Subscription: " + subscription.getActiveSubscriptions());
//                    }
//                    case final Candle candle -> {
//                        System.out.println("Candle: " + candle);
//                    }
//                    case final Trade trade -> {
//                        System.out.println("Trade: " + trade);
//                    }
//                    case final Ticker24h ticker24h -> {
//                        System.out.println("Ticker24h: " + ticker24h);
//                    }
//                    default -> {
//                        System.out.println("Unhandled type: " + message.getClass().getSimpleName());
//                    }
//                }
//            });

        Thread.currentThread().join();
    }
}
