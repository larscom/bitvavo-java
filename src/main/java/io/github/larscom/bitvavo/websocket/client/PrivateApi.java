package io.github.larscom.bitvavo.websocket.client;

import io.github.larscom.bitvavo.websocket.account.Fill;
import io.github.larscom.bitvavo.websocket.account.Order;
import io.reactivex.rxjava3.core.Flowable;

public interface PrivateApi extends PublicApi {
    /// Receive order data after subscribing to the account channel.
    ///
    /// <pre>
    /// {@code
    /// final PrivateApi client = ReactiveWebSocketClient.newPrivate(new Credentials("API_KEY", "API_SECRET"));
    ///
    /// final Set<Channel> channels = Set.of(
    ///     Channel.builder().name(ChannelName.ACCOUNT).markets(Set.of("ETH-EUR")).build()
    /// );
    ///
    /// client.subscribe(channels);
    ///
    /// // receive order data
    /// client.orders().subscribe(System.out::println);
    /// }
    /// </pre>
    Flowable<Order> orders();

    /// Receive fill data after subscribing to the account channel.
    ///
    /// <pre>
    /// {@code
    /// final PrivateApi client = ReactiveWebSocketClient.newPrivate(new Credentials("API_KEY", "API_SECRET"));
    ///
    /// final Set<Channel> channels = Set.of(
    ///     Channel.builder().name(ChannelName.ACCOUNT).markets(Set.of("ETH-EUR")).build()
    /// );
    ///
    /// client.subscribe(channels);
    ///
    /// // receive fill data
    /// client.fills().subscribe(System.out::println);
    /// }
    /// </pre>
    Flowable<Fill> fills();
}
