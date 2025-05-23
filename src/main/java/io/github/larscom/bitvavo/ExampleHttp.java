package io.github.larscom.bitvavo;

import io.github.larscom.bitvavo.http.ReactiveApiClient;

class ExampleHttp {
    public static void main(final String[] args) throws InterruptedException {
        final var client = new ReactiveApiClient();

        client.getTime().subscribe((theTime, throwable) -> {
            System.out.println(theTime);
        });

        client.getMarkets().subscribe((markets, throwable) -> {
            System.out.println(markets.stream().filter(m -> m.getMarket().equals("HONEY-EUR")).findFirst());
        });

        Thread.currentThread().join();
    }
}
