package io.github.larscom.bitvavo.trade;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Side {
    @JsonProperty("buy")
    BUY,

    @JsonProperty("sell")
    SELL
}

