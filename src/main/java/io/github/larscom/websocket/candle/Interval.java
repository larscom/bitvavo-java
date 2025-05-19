package io.github.larscom.websocket.candle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Interval {
    M1("1m"),
    M5("5m"),
    M15("15m"),
    M30("30m"),
    H1("1h"),
    H2("2h"),
    H4("4h"),
    H6("6h"),
    H8("8h");

    private final String interval;

    Interval(final String interval) {
        this.interval = interval;
    }

    @JsonValue
    public String serialize() {
        return interval;
    }

    @JsonCreator
    public static Interval deserialize(final String interval) {
        return Arrays.stream(values())
            .filter(i -> i.interval.equals(interval)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid interval: " + interval));
    }
}
