package io.github.larscom.bitvavo.websocket.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SelfTradePrevention {
    @JsonProperty("decrementAndCancel")
    DECREMENT_AND_CANCEL,

    @JsonProperty("cancelOldest")
    CANCEL_OLDEST,

    @JsonProperty("cancelNewest")
    CANCEL_NEWEST,

    @JsonProperty("cancelBoth")
    CANCEL_BOTH
}

