package io.github.larscom.bitvavo;

import io.github.larscom.bitvavo.http.client.ReactiveApiClient;

class ExampleHttp {
    public static void main(final String[] args) throws InterruptedException {
        final var client = new ReactiveApiClient();

        client.getTime().subscribe((theTime, throwable) -> {
            System.out.println(theTime);
        });

        client.getMarket("BTC-EUR").subscribe((markets, throwable) -> {
            System.out.println(markets);
        });

        client.getAsset("BTC").subscribe((assets, throwable) -> {
            System.out.println(assets);
        });

        Thread.currentThread().join();
    }
}
