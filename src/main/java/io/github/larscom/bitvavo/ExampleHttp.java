package io.github.larscom.bitvavo;

import io.github.larscom.bitvavo.http.candle.Interval;
import io.github.larscom.bitvavo.http.client.ApiClientConfig;
import io.github.larscom.bitvavo.http.client.ReactiveApiClient;

class ExampleHttp {
    public static void main(final String[] args) throws InterruptedException {
        final var config = ApiClientConfig.builder().build();
        final var client = new ReactiveApiClient(config);

        client.getTime().subscribe((time, throwable) -> {
            System.out.println(time);
        });

        client.getMarket("BTC-EUR").subscribe((markets, throwable) -> {
            System.out.println(markets);
        });

        client.getAsset("BTC").subscribe((assets, throwable) -> {
            System.out.println(assets);
        });

        client.getOrderBook("ETH-EUR").subscribe((book, throwable) -> {
            System.out.println(book);
        });

        client.getTrades("ETH-EUR").subscribe((trades, throwable) -> {
            System.out.println(trades);
        });

        client.getTickerPrices().subscribe((tickerPrices, throwable) -> {
            System.out.println(tickerPrices);
        });

        client.getTickerPrice("ETH-EUR").subscribe((tickerPrice, throwable) -> {
            System.out.println(tickerPrice);
        });

        client.getTickerBooks().subscribe((tickerBooks, throwable) -> {
            System.out.println(tickerBooks);
        });

        client.getTickerBook("ETH-EUR").subscribe((tickerBook, throwable) -> {
            System.out.println(tickerBook);
        });

        client.getCandles("ETH-EUR", Interval.M1).subscribe((candles, throwable) -> {
            System.out.println(candles);
        });

        Thread.currentThread().join();
    }
}
