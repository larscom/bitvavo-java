package io.github.larscom.bitvavo.http.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.util.Optional;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableWithdrawalHistory.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface WithdrawalHistory {
    /// Timestamp in unix milliseconds.
    Long getTimestamp();

    /// The asset that was returned. (e.g: "ETH")
    String getSymbol();

    /// Amount that has been withdrawn.
    BigDecimal getAmount();

    /// Address that has been used for this withdrawal.
    String getAddress();

    /// Payment ID used for this withdrawal. This is mostly called a note, memo or tag.
    Optional<String> getPaymentId();

    /// The transaction ID, which can be found on the blockchain, for this specific withdrawal.
    @JsonProperty("txId")
    Optional<String> getTransactionId();

    /// The fee which has been paid to withdraw this currency.
    BigDecimal getFee();

    /// The status of the withdrawal.
    WithdrawalHistoryStatus getStatus();
}
