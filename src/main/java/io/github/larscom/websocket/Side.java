package io.github.larscom.websocket;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Side {
    BUY, SELL;

    @JsonValue
    public String serialize() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static Side deserialize(final String side) {
        return Side.valueOf(side.toUpperCase());
    }
}

