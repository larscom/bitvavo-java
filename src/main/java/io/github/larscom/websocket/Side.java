package io.github.larscom.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Side {
    @JsonProperty("buy")
    BUY,

    @JsonProperty("sell")
    SELL
}

