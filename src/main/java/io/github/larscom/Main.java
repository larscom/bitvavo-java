package io.github.larscom;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.ws.*;

import java.util.List;

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
                    default -> {
                    }
                }
            });

        listener.subscribe(List.of("ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR"));

        Thread.currentThread().join();
    }
}
