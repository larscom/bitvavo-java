package io.github.larscom.bitvavo.http.market;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MarketStatus {
    @JsonProperty("trading")
    TRADING,

    @JsonProperty("halted")
    HALTED,

    @JsonProperty("auction")
    AUCTION
}
