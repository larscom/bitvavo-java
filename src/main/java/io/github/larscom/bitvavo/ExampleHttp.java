package io.github.larscom.bitvavo;

import io.github.larscom.bitvavo.http.ReactiveApiClient;

class ExampleHttp {
    public static void main(final String[] args) throws InterruptedException {
        final var client = new ReactiveApiClient();

        client.getTime().subscribe((theTime, throwable) -> {
            System.out.println(theTime);
            System.out.println("Rate limit (getTime): " + client.getRateLimit());
        });

        client.getMarket("BTC-EUR").subscribe((markets, throwable) -> {
            System.out.println(markets);
            System.out.println("Rate limit (getMarket): " + client.getRateLimit());
        });

        Thread.currentThread().join();
    }
}
