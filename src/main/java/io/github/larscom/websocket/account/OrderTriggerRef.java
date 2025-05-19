package io.github.larscom.websocket.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderTriggerRef {
    @JsonProperty("lastTrade")
    LAST_TRADE,

    @JsonProperty("bestBid")
    BEST_BID,

    @JsonProperty("bestAsk")
    BEST_ASK,

    @JsonProperty("midPrice")
    MID_PRICE
}
