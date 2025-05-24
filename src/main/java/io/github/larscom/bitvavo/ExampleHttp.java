package io.github.larscom.bitvavo;

import io.github.larscom.bitvavo.http.trade.TradeQueryParams;
import io.github.larscom.bitvavo.http.client.ReactiveApiClient;

import java.util.UUID;

class ExampleHttp {
    public static void main(final String[] args) throws InterruptedException {
        final var client = new ReactiveApiClient();

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

        final var params = TradeQueryParams.builder().tradeIdTo(UUID.fromString("00000000-0000-047b-0000-0000016cac2f")).build();
        client.getTrades("ETH-EUR", params).subscribe((trades, throwable) -> {
            if (throwable != null) {
                System.err.println(throwable.getMessage());
            } else {
                System.out.println(trades);
            }
        });

        Thread.currentThread().join();
    }
}
