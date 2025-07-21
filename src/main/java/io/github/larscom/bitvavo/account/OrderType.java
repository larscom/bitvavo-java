package io.github.larscom.bitvavo.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderType {
    @JsonProperty("market")
    MARKET,

    @JsonProperty("limit")
    LIMIT,

    @JsonProperty("stopLoss")
    STOP_LOSS,

    @JsonProperty("stopLossLimit")
    STOP_LOSS_LIMIT,

    @JsonProperty("takeProfit")
    TAKE_PROFIT,

    @JsonProperty("takeProfitLimit")
    TAKE_PROFIT_LIMIT
}

