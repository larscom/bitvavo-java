package io.github.larscom.bitvavo.websocket.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderTriggerType {
    @JsonProperty("price")
    PRICE
}
