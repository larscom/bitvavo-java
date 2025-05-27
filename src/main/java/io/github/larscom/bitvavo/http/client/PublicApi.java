package io.github.larscom.bitvavo.http.client;

import io.github.larscom.bitvavo.http.asset.Asset;
import io.github.larscom.bitvavo.http.book.Book;
import io.github.larscom.bitvavo.http.candle.Candle;
import io.github.larscom.bitvavo.http.candle.Candle24h;
import io.github.larscom.bitvavo.http.candle.CandleQueryParams;
import io.github.larscom.bitvavo.http.candle.Interval;
import io.github.larscom.bitvavo.http.market.Market;
import io.github.larscom.bitvavo.http.ticker.TickerBook;
import io.github.larscom.bitvavo.http.ticker.TickerPrice;
import io.github.larscom.bitvavo.http.trade.Trade;
import io.github.larscom.bitvavo.http.trade.TradeQueryParams;
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

    Single<List<Asset>> getAssets();

    Single<Asset> getAsset(String symbol);

    Single<Book> getOrderBook(String market);

    Single<Book> getOrderBook(String market, int depth);

    Single<List<Trade>> getTrades(String market);

    Single<List<Trade>> getTrades(String market, TradeQueryParams queryParams);

    Single<List<TickerPrice>> getTickerPrices();

    Single<TickerPrice> getTickerPrice(String market);

    Single<List<TickerBook>> getTickerBooks();

    Single<TickerBook> getTickerBook(String market);

    Single<List<Candle>> getCandles(String market, Interval interval);

    Single<List<Candle>> getCandles(String market, Interval interval, CandleQueryParams queryParams);

    Single<List<Candle24h>> getCandle24h();

    Single<Candle24h> getCandle24h(String market);
}
