package io.github.larscom.bitvavo.http.client;

import io.github.larscom.bitvavo.candle.Interval;
import io.github.larscom.bitvavo.http.asset.Asset;
import io.github.larscom.bitvavo.http.book.Book;
import io.github.larscom.bitvavo.http.candle.Candle;
import io.github.larscom.bitvavo.http.candle.Candle24h;
import io.github.larscom.bitvavo.http.candle.CandleParams;
import io.github.larscom.bitvavo.http.market.Market;
import io.github.larscom.bitvavo.http.ticker.TickerBook;
import io.github.larscom.bitvavo.http.ticker.TickerPrice;
import io.github.larscom.bitvavo.http.trade.Trade;
import io.github.larscom.bitvavo.http.trade.TradeParams;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

public interface PublicApi {
    /// Returns the current rate limit quota.
    RateLimitQuota getCurrentRateLimitQuota();

    /// Returns a stream of rate limit quota, a value is emitted everytime the quota updates.
    Observable<RateLimitQuota> getRateLimitQuota();

    /// Returns the current Unix timestamp of Bitvavo servers.
    ///
    /// Rate limit weight points: 1
    Single<Long> getTime();

    /// Returns the information about all markets on Bitvavo.
    ///
    /// Rate limit weight points: 1
    Single<List<Market>> getMarkets();

    /// Returns the information about one market on Bitvavo.
    ///
    /// Rate limit weight points: 1
    Single<Market> getMarket(String market);

    /// Returns the information about the specified assets.
    ///
    /// Rate limit weight points: 1
    Single<List<Asset>> getAssets();

    /// Returns the information about the specified asset based on symbol.
    ///
    /// Rate limit weight points: 1
    Single<Asset> getAsset(String symbol);

    /// Returns the list of all bids and asks for the specified market, sorted by price.
    ///
    /// Rate limit weight points: 1
    Single<Book> getOrderBook(String market);

    /// Returns the list of all bids and asks for the specified market with specific depth, sorted by price.
    ///
    /// Rate limit weight points: 1
    Single<Book> getOrderBook(String market, int depth);

    /// Returns the list of trades for specified market and time period made by all Bitvavo users.
    ///
    /// Rate limit weight points: 5
    Single<List<Trade>> getTrades(String market);

    /// Returns the list of trades for specified market and time period made by all Bitvavo users.
    ///
    /// Rate limit weight points: 5
    Single<List<Trade>> getTrades(String market, TradeParams tradeParams);

    /// Returns prices of the latest trades on Bitvavo for all markets.
    ///
    /// Rate limit weight points: 1
    Single<List<TickerPrice>> getTickerPrices();

    /// Returns prices of the latest trades on Bitvavo for a single market.
    ///
    /// Rate limit weight points: 1
    Single<TickerPrice> getTickerPrice(String market);

    /// Returns the highest buy and the lowest sell prices currently available for all markets in the Bitvavo order book.
    ///
    /// Rate limit weight points: 1
    Single<List<TickerBook>> getTickerBooks();

    /// Returns the highest buy and the lowest sell prices currently available for a single market in the Bitvavo order book.
    /// Rate limit weight points: 1
    Single<TickerBook> getTickerBook(String market);

    /// Returns the OHLCV market data used to create candlestick charts.
    ///
    /// Candlestick data is always returned in chronological data from newest to oldest.
    /// Data is returned when trades are made in the interval represented by that candlestick.
    /// When no trades occur you see a gap in data flow, zero trades are represented by zero candlesticks.
    ///
    /// Rate limit weight points: 1
    Single<List<Candle>> getCandles(String market, Interval interval);

    /// Returns the OHLCV market data used to create candlestick charts.
    ///
    /// Candlestick data is always returned in chronological data from newest to oldest.
    /// Data is returned when trades are made in the interval represented by that candlestick.
    /// When no trades occur you see a gap in data flow, zero trades are represented by zero candlesticks.
    ///
    /// Rate limit weight points: 1
    Single<List<Candle>> getCandles(String market, Interval interval, CandleParams candleParams);

    /// Returns the OHLCV data about trades and orders for all markets on Bitvavo during the latest 24 hours.
    ///
    /// Rate limit weight points: 25
    Single<List<Candle24h>> getCandle24h();

    /// Returns the OHLCV data about trades and orders for a single market on Bitvavo during the latest 24 hours.
    ///
    /// Rate limit weight points: 1
    Single<Candle24h> getCandle24h(String market);
}
