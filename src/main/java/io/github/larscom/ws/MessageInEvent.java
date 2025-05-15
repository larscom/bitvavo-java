package io.github.larscom.ws;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageInEvent {
    SUBSCRIBED,
    UNSUBSCRIBED,
    CANDLE,
    TICKER,
    TICKER24H,
    TRADE,
    BOOK,
    AUTHENTICATE,
    ORDER,
    FILL;

    @JsonValue
    public String serialize() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static MessageInEvent deserialize(final String event) {
        return MessageInEvent.valueOf(event.toUpperCase());
    }
}

