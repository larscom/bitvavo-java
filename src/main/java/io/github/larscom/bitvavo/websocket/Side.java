package io.github.larscom.bitvavo.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Side {
    @JsonProperty("buy")
    BUY,

    @JsonProperty("sell")
    SELL
}

