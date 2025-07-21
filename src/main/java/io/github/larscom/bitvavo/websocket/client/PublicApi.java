package io.github.larscom.bitvavo.websocket.client;

import io.github.larscom.bitvavo.error.BitvavoError;
import io.github.larscom.bitvavo.websocket.Trade;
import io.github.larscom.bitvavo.websocket.book.Book;
import io.github.larscom.bitvavo.websocket.candle.Candle;
import io.github.larscom.bitvavo.websocket.channel.Channel;
import io.github.larscom.bitvavo.websocket.message.MessageIn;
import io.github.larscom.bitvavo.websocket.subscription.Subscription;
import io.github.larscom.bitvavo.websocket.ticker.Ticker;
import io.github.larscom.bitvavo.websocket.ticker.Ticker24h;
import io.reactivex.rxjava3.core.Flowable;

import java.util.Set;

public interface PublicApi {
    /// Receive all messages within a single stream, use `instanceof` to determine the type.
    ///
    /// <pre>
    /// {@code
    /// final PublicApi client = ReactiveWebSocketClient.newPublic();
    ///
    /// final Set<Channel> channels = Set.of(
    ///     Channel.builder().name(ChannelName.TICKER).markets(Set.of("ETH-EUR")).build(),
    ///     Channel.builder().name(ChannelName.BOOK).markets(Set.of("ETH-EUR")).build()
    /// );
    ///
    /// client.subscribe(channels);
    ///
    /// // single stream to handle all message types.
    /// client.messages().subscribe(messageIn -> {
    ///   if (messageIn instanceof final Ticker ticker) {
    ///     System.out.println("Ticker: " + ticker);
    ///   }
    ///
    ///   if (messageIn instanceof final Book book) {
    ///     System.out.println("Book: " + book);
    ///   }
    /// });
    /// }
    /// </pre>
    Flowable<MessageIn> messages();

    /// Receive ticker data after subscribing to the ticker channel.
    ///
    /// <pre>
    /// {@code
    /// final PublicApi client = ReactiveWebSocketClient.newPublic();
    ///
    /// final Set<Channel> channels = Set.of(
    ///     Channel.builder().name(ChannelName.TICKER).markets(Set.of("ETH-EUR")).build()
    /// );
    ///
    /// client.subscribe(channels);
    ///
    /// // receive ticker data
    /// client.tickers().subscribe(System.out::println);
    /// }
    /// </pre>
    Flowable<Ticker> tickers();

    /// Receive ticker24h data after subscribing to the ticker24h channel.
    ///
    /// <pre>
    /// {@code
    /// final PublicApi client = ReactiveWebSocketClient.newPublic();
    ///
    /// final Set<Channel> channels = Set.of(
    ///     Channel.builder().name(ChannelName.TICKER24H).markets(Set.of("ETH-EUR")).build()
    /// );
    ///
    /// client.subscribe(channels);
    ///
    /// // receive ticker24h data
    /// client.tickers24h().subscribe(System.out::println);
    /// }
    /// </pre>
    Flowable<Ticker24h> tickers24h();

    /// Receive book data after subscribing to the book channel.
    ///
    /// <pre>
    /// {@code
    /// final PublicApi client = ReactiveWebSocketClient.newPublic();
    ///
    /// final Set<Channel> channels = Set.of(
    ///     Channel.builder().name(ChannelName.BOOK).markets(Set.of("ETH-EUR")).build()
    /// );
    ///
    /// client.subscribe(channels);
    ///
    /// // receive book data
    /// client.books().subscribe(System.out::println);
    /// }
    /// </pre>
    Flowable<Book> books();

    /// Receive the active subscriptions after subscribing to channels.
    ///
    /// <pre>
    /// {@code
    /// final PublicApi client = ReactiveWebSocketClient.newPublic();
    ///
    /// final Set<Channel> channels = Set.of(
    ///     Channel.builder().name(ChannelName.BOOK).markets(Set.of("ETH-EUR")).build(),
    ///     Channel.builder().name(ChannelName.CANDLES).intervals(Set.of(Interval.M1)).markets(Set.of("ETH-EUR")).build()
    ///
    /// );
    ///
    /// client.subscribe(channels);
    ///
    /// client.subscriptions()
    ///         .map(Subscription::getActiveSubscriptions)
    ///         .subscribe(subscriptions -> {
    ///             subscriptions.forEach((channelName, subscription) -> {
    ///                 System.out.println("Channel: " + channelName);
    ///                 // only subscribed markets
    ///                 if (subscription instanceof final SubscriptionWithMarkets value) {
    ///                     System.out.println("Subscribed markets: " + value.getMarkets());
    ///                 }
    ///                 // when subscribed to candles channel you get back the interval as well.
    ///                 if (subscription instanceof final SubscriptionWithInterval value) {
    ///                     final Set<Interval> intervals = value.getIntervalWithMarkets().keySet();
    ///                     final Set<String> markets = value.getIntervalWithMarkets()
    ///                         .values()
    ///                         .stream()
    ///                         .flatMap(Set::stream)
    ///                         .collect(Collectors.toSet());
    ///                      System.out.printf("Subscribed markets: %s with intervals: %s%n", markets, intervals);
    ///                 }
    ///             });
    ///     });
    /// }
    /// </pre>
    Flowable<Subscription> subscriptions();

    /// Receive candle data after subscribing to the candle channel.
    ///
    /// <pre>
    /// {@code
    /// final PublicApi client = ReactiveWebSocketClient.newPublic();
    ///
    /// final Set<Channel> channels = Set.of(
    ///     Channel.builder().name(ChannelName.CANDLES).intervals(Set.of(Interval.M1)).markets(Set.of("ETH-EUR")).build()
    /// );
    ///
    /// client.subscribe(channels);
    ///
    /// // receive candle data
    /// client.candles().subscribe(System.out::println);
    /// }
    /// </pre>
    Flowable<Candle> candles();

    /// Receive trade data after subscribing to the trade channel.
    ///
    /// <pre>
    /// {@code
    /// final PublicApi client = ReactiveWebSocketClient.newPublic();
    ///
    /// final Set<Channel> channels = Set.of(
    ///     Channel.builder().name(ChannelName.TRADE).markets(Set.of("ETH-EUR")).build()
    /// );
    ///
    /// client.subscribe(channels);
    ///
    /// // receive trade data
    /// client.books().subscribe(System.out::println);
    /// }
    /// </pre>
    Flowable<Trade> trades();

    /// Receive errors that occur, for example while subscribing to channels with a wrong market.
    ///
    /// <pre>
    /// {@code
    /// final PublicApi client = ReactiveWebSocketClient.newPublic();
    ///
    /// final Set<Channel> channels = Set.of(
    ///     Channel.builder().name(ChannelName.TRADE).markets(Set.of("WRONG-MARKET")).build()
    /// );
    ///
    /// client.subscribe(channels);
    ///
    /// // receive errors
    /// client.errors().subscribe(System.out::println);
    /// }
    /// </pre>
    Flowable<BitvavoError> errors();

    /// Subscribe to channels, this can be called multiple times with different channels and/or markets.
    void subscribe(Set<Channel> channels);

    /// Unsubscribe from channels, this can be called multiple times with different channels and/or markets.
    void unsubscribe(Set<Channel> channels);

    /// Close the websocket connection.
    void close();
}
