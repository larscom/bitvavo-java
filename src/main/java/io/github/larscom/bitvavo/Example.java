package io.github.larscom.bitvavo;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.bitvavo.http.ReactiveApiClient;
import io.github.larscom.bitvavo.http.market.Market;
import io.github.larscom.bitvavo.http.market.MarketStatus;
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

        final ReactiveWebSocketClient webSocketClient;

        if (credentials.isPresent()) {
            webSocketClient = new ReactiveWebSocketClient(credentials.get());

            webSocketClient.subscribe(Set.of(Channel.builder().name(ChannelName.ACCOUNT).markets(Set.of("ETH-EUR", "BTC-EUR")).build()));

            webSocketClient.orders().subscribe(System.out::println);
            webSocketClient.fills().subscribe(System.out::println);
        } else {
            webSocketClient = new ReactiveWebSocketClient();
        }

        final var apiClient = new ReactiveApiClient();

        final var markets = apiClient.getMarkets().blockingGet()
            .stream()
            .filter(market -> market.getStatus() == MarketStatus.TRADING)
            .map(Market::getMarket)
            .toList();

        final var channels = Set.of(
            Channel.builder().name(ChannelName.TICKER).markets(markets).build(),
            Channel.builder().name(ChannelName.TICKER24H).markets(markets).build(),
            Channel.builder().name(ChannelName.BOOK).markets(markets).build(),
            Channel.builder().name(ChannelName.TRADES).markets(markets).build(),
            Channel.builder().name(ChannelName.CANDLES).intervals(Set.of(Interval.M1)).markets(markets).build()
        );
        webSocketClient.subscribe(channels);

        // receive errors, mostly for debug purposes
        webSocketClient.errors().subscribe(System.out::println);

        // receive data
        webSocketClient.tickers().subscribe(System.out::println);
        webSocketClient.tickers24h().subscribe(System.out::println);
        webSocketClient.books().subscribe(System.out::println);
        webSocketClient.subscriptions().subscribe(System.out::println);
        webSocketClient.candles().subscribe(System.out::println);
        webSocketClient.trades().subscribe(System.out::println);

        Thread.currentThread().join();
    }
}
