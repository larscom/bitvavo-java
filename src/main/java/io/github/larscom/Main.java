package io.github.larscom;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.ws.*;

public class Main {

    public static void main(final String[] args) throws InterruptedException, JsonProcessingException {
        final var listener = new WebSocketListener();

        listener.stream()
            .filter(Either::isLeft)
            .map(Either::getLeft)
            .subscribe(message -> {
                switch (message) {
                    case final Ticker ticker -> {
                        System.out.println("Ticker: " + ticker);
                    }
                    case final Subscription subscription -> {
                        System.out.println("Subscribed: " + subscription);
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + message);
                }
            });

        listener.subscribe();

        Thread.currentThread().join();
    }
}
