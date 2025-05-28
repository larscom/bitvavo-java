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
    Flowable<MessageIn> messages();

    Flowable<Ticker> tickers();

    Flowable<Ticker24h> tickers24h();

    Flowable<Book> books();

    Flowable<Subscription> subscriptions();

    Flowable<Candle> candles();

    Flowable<Trade> trades();

    Flowable<BitvavoError> errors();

    void subscribe(Set<Channel> channels);

    void unsubscribe(Set<Channel> channels);

    void close();
}
