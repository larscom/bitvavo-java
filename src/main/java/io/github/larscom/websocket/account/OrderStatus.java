package io.github.larscom.websocket.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.github.larscom.internal.CaseConverter;

import java.util.Arrays;

public enum OrderStatus {
    NEW,
    AWAITING_TRIGGER,
    CANCELED,
    CANCELED_AUCTION,
    CANCELED_SELF_TRADE_PREVENTION,
    CANCELED_IOC,
    CANCELED_FOK,
    CANCELED_MARKET_PROTECTION,
    CANCELED_POST_ONLY,
    FILLED,
    PARTIALLY_FILLED,
    EXPIRED,
    REJECTED;

    @JsonValue
    public String serialize() {
        return CaseConverter.toCamelCase(name());
    }

    @JsonCreator
    public static OrderStatus deserialize(final String orderStatus) {
        return Arrays.stream(values())
            .filter(status -> status.serialize().equals(orderStatus)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid orderStatus: " + orderStatus));
    }
}
