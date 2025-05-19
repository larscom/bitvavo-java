package io.github.larscom;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.websocket.Channel;
import io.github.larscom.websocket.ChannelName;
import io.github.larscom.websocket.Credentials;
import io.github.larscom.websocket.Interval;
import io.github.larscom.websocket.client.ReactiveWebSocketClient;

import java.util.Optional;
import java.util.Set;

class Example {

    public static void main(final String[] args) throws InterruptedException, JsonProcessingException {
        final var apiKey = Optional.ofNullable(System.getenv("API_KEY"));
        final var apiSecret = Optional.ofNullable(System.getenv("API_SECRET"));

        final var credentials = apiKey.flatMap(key ->
            apiSecret.map(secret -> new Credentials(key, secret))
        );

        final ReactiveWebSocketClient client;
        if (credentials.isPresent()) {
            client = new ReactiveWebSocketClient(credentials.get());
        } else {
            client = new ReactiveWebSocketClient();
        }

        final var channels = Set.of(
            Channel.builder().name(ChannelName.ACCOUNT).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build(),
            Channel.builder().name(ChannelName.TICKER).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build(),
            Channel.builder().name(ChannelName.TICKER24H).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build(),
            Channel.builder().name(ChannelName.BOOK).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build(),
            Channel.builder().name(ChannelName.TRADES).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build(),
            Channel.builder().name(ChannelName.CANDLES).intervals(Set.of(Interval.M1)).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build()
        );

        client.subscribe(channels);

        client.error().subscribe(System.out::println);

        client.ticker().subscribe(System.out::println);
        client.ticker24h().subscribe(System.out::println);
        client.book().subscribe(System.out::println);
        client.subscription().subscribe(System.out::println);
        client.candles().subscribe(System.out::println);
        client.trades().subscribe(System.out::println);

        Thread.currentThread().join();
    }
}
