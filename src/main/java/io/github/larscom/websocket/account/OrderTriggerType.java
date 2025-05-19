package io.github.larscom.websocket.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderTriggerType {
    @JsonProperty("price")
    PRICE
}
