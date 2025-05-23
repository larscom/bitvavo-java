package io.github.larscom.bitvavo.websocket.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

enum Action {
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
