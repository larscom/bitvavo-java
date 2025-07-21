package io.github.larscom.bitvavo.http.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TransactionType {
    @JsonProperty("sell")
    SELL,

    @JsonProperty("buy")
    BUY,

    @JsonProperty("staking")
    STAKING,

    @JsonProperty("deposit")
    DEPOSIT,

    @JsonProperty("withdrawal")
    WITHDRAWAL,

    @JsonProperty("affiliate")
    AFFILIATE,

    @JsonProperty("distribution")
    DISTRIBUTION,

    @JsonProperty("internal_transfer")
    INTERNAL_TRANSFER,

    @JsonProperty("withdrawal_cancelled")
    WITHDRAWAL_CANCELLED,

    @JsonProperty("rebate")
    REBATE,

    @JsonProperty("loan")
    LOAN,

    @JsonProperty("external_transferred_funds")
    EXTERNAL_TRANSFERRED_FUNDS,

    @JsonProperty("manually_assigned_bitvavo")
    MANUALLY_ASSIGNED_BITVAVO
}
