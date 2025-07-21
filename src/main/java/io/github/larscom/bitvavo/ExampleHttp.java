package io.github.larscom.bitvavo;

import io.github.larscom.bitvavo.account.Credentials;
import io.github.larscom.bitvavo.http.client.ReactiveHttpClient;


class ExampleHttp {
    public static void main(final String[] args) throws InterruptedException {
        final var client = ReactiveHttpClient.newPrivate(new Credentials(System.getenv("API_KEY"), System.getenv("API_SECRET")));

        client.getAccountFee().subscribe((history, throwable) -> {
            System.out.println(history);
            System.out.println(throwable);
        });

//        client.getTime().subscribe((time, throwable) -> {
//            System.out.println(time);
//        });
//
//        client.getMarket("BTC-EUR").subscribe((markets, throwable) -> {
//            System.out.println(markets);
//        });
//
//        client.getAsset("BTC").subscribe((assets, throwable) -> {
//            System.out.println(assets);
//        });
//
//        client.getOrderBook("ETH-EUR").subscribe((book, throwable) -> {
//            System.out.println(book);
//        });
//
//        client.getTrades("ETH-EUR").subscribe((trades, throwable) -> {
//            System.out.println(trades);
//        });
//
//        client.getTickerPrices().subscribe((tickerPrices, throwable) -> {
//            System.out.println(tickerPrices);
//        });
//
//        client.getTickerPrice("ETH-EUR").subscribe((tickerPrice, throwable) -> {
//            System.out.println(tickerPrice);
//        });
//
//        client.getTickerBooks().subscribe((tickerBooks, throwable) -> {
//            System.out.println(tickerBooks);
//        });
//
//        client.getTickerBook("ETH-EUR").subscribe((tickerBook, throwable) -> {
//            System.out.println(tickerBook);
//        });
//
//        client.getCandle24h("ETH-EUR").subscribe((candles, throwable) -> {
//            System.out.println(candles);
//        });

        Thread.currentThread().join();
    }
}
