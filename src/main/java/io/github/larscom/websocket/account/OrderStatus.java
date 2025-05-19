package io.github.larscom.websocket.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderStatus {
    @JsonProperty("new")
    NEW,

    @JsonProperty("awaitingTrigger")
    AWAITING_TRIGGER,

    @JsonProperty("canceled")
    CANCELED,

    @JsonProperty("canceledAuction")
    CANCELED_AUCTION,

    @JsonProperty("canceledSelfTradePrevention")
    CANCELED_SELF_TRADE_PREVENTION,

    @JsonProperty("canceledIoc")
    CANCELED_IOC,

    @JsonProperty("canceledFok")
    CANCELED_FOK,

    @JsonProperty("canceledMarketProtection")
    CANCELED_MARKET_PROTECTION,

    @JsonProperty("canceledPostOnly")
    CANCELED_POST_ONLY,

    @JsonProperty("filled")
    FILLED,

    @JsonProperty("partiallyFilled")
    PARTIALLY_FILLED,

    @JsonProperty("expired")
    EXPIRED,

    @JsonProperty("rejected")
    REJECTED
}

