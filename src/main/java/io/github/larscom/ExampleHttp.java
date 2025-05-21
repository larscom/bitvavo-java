package io.github.larscom;


import io.github.larscom.http.ReactiveApiClient;
import io.reactivex.rxjava3.schedulers.Schedulers;

class ExampleHttp {
    public static void main(final String[] args) throws InterruptedException {
        final var client = new ReactiveApiClient();

        client.getTime().subscribeOn(Schedulers.io()).subscribe((theTime, throwable) -> {
            System.out.println(theTime);
        });

        client.getMarkets().subscribeOn(Schedulers.io()).subscribe((theTime, throwable) -> {
            System.out.println(theTime);
        });

        Thread.currentThread().join();
    }
}
