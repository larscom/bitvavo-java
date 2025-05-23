package io.github.larscom.bitvavo;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.bitvavo.websocket.Channel;
import io.github.larscom.bitvavo.websocket.ChannelName;
import io.github.larscom.bitvavo.websocket.account.Credentials;
import io.github.larscom.bitvavo.websocket.candle.Interval;
import io.github.larscom.bitvavo.websocket.client.ReactiveWebSocketClient;

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

            client.subscribe(Set.of(Channel.builder().name(ChannelName.ACCOUNT).markets(Set.of("ETH-EUR", "BTC-EUR")).build()));

            client.orders().subscribe(System.out::println);
            client.fills().subscribe(System.out::println);
        } else {
            client = new ReactiveWebSocketClient();
        }

        final var channels = Set.of(
            Channel.builder().name(ChannelName.TICKER).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build(),
            Channel.builder().name(ChannelName.TICKER24H).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build(),
            Channel.builder().name(ChannelName.BOOK).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build(),
            Channel.builder().name(ChannelName.TRADES).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build(),
            Channel.builder().name(ChannelName.CANDLES).intervals(Set.of(Interval.M1)).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build()
        );
        client.subscribe(channels);

        // receive errors, mostly for debug purposes
        client.errors().subscribe(System.out::println);

        // receive data
        client.tickers().subscribe(System.out::println);
        client.tickers24h().subscribe(System.out::println);
        client.books().subscribe(System.out::println);
        client.subscriptions().subscribe(System.out::println);
        client.candles().subscribe(System.out::println);
        client.trades().subscribe(System.out::println);

        Thread.currentThread().join();
    }
}
