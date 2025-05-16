package io.github.larscom.websocket;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Action {
    AUTHENTICATE, SUBSCRIBE, UNSUBSCRIBE;

    @JsonValue
    public String serialize() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static Action deserialize(final String action) {
        return Action.valueOf(action.toUpperCase());
    }
}
