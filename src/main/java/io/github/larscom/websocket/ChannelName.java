package io.github.larscom.websocket;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ChannelName {
    ACCOUNT,
    CANDLES,
    BOOK,
    TICKER,
    TICKER24H,
    TRADES;

    @JsonValue
    public String serialize() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static ChannelName deserialize(final String event) {
        return ChannelName.valueOf(event.toUpperCase());
    }
}

