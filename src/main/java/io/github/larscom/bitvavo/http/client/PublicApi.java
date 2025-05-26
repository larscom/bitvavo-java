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
    RateLimitQuota getCurrentRateLimitQuota();

    Observable<RateLimitQuota> getRateLimitQuota();

    Single<Long> getTime();

    Single<List<Market>> getMarkets();

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
