package io.github.larscom.bitvavo.http.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum WithdrawalHistoryStatus {
    @JsonProperty("awaiting_processing")
    AWAITING_PROCESSING,

    @JsonProperty("awaiting_email_confirmation")
    AWAITING_EMAIL_CONFIRMATION,

    @JsonProperty("awaiting_bitvavo_inspection")
    AWAITING_BITVAVO_INSPECTION,

    @JsonProperty("approved")
    APPROVED,

    @JsonProperty("sending")
    SENDING,

    @JsonProperty("in_mempool")
    IN_MEMPOOL,

    @JsonProperty("processed")
    PROCESSED,

    @JsonProperty("completed")
    COMPLETED,

    @JsonProperty("canceled")
    CANCELED
}
