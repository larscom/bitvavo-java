package io.github.larscom.bitvavo;

import io.github.larscom.bitvavo.account.Credentials;
import io.github.larscom.bitvavo.candle.Interval;
import io.github.larscom.bitvavo.websocket.channel.Channel;
import io.github.larscom.bitvavo.websocket.channel.ChannelName;
import io.github.larscom.bitvavo.websocket.client.PrivateApi;
import io.github.larscom.bitvavo.websocket.client.PublicApi;
import io.github.larscom.bitvavo.websocket.client.ReactiveWebSocketClient;

import java.util.Optional;
import java.util.Set;

class Example {

    public static void main(final String[] args) throws InterruptedException {
        final var apiKey = Optional.ofNullable(System.getenv("API_KEY"));
        final var apiSecret = Optional.ofNullable(System.getenv("API_SECRET"));

        final var credentials = apiKey.flatMap(key -> apiSecret.map(secret -> new Credentials(key, secret)));

        credentials.ifPresentOrElse(Example::withPrivateApi, Example::withPublicApi);

        Thread.currentThread().join();
    }

    private static void withPublicApi() {
        // Public client only contains public endpoints
        final PublicApi client = ReactiveWebSocketClient.newPublic();

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
    }

    private static void withPrivateApi(final Credentials credentials) {
        // Private client also contains all public endpoints
        final PrivateApi client = ReactiveWebSocketClient.newPrivate(credentials);

        final var channels = Set.of(
            Channel.builder().name(ChannelName.ACCOUNT).markets(Set.of("ETH-EUR", "BTC-EUR")).build(),
            Channel.builder().name(ChannelName.TICKER).markets(Set.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR")).build()
        );
        client.subscribe(channels);

        // receive errors, mostly for debug purposes
        client.errors().subscribe(System.out::println);

        // receive private data
        client.orders().subscribe(System.out::println);
        client.fills().subscribe(System.out::println);

        // receive public data
        client.tickers().subscribe(System.out::println);
    }
}
