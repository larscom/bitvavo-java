package io.github.larscom.bitvavo.http.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableTransaction.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Transaction {
    /// The unique identifier of the transaction.
    UUID getTransactionId();

    /// The Unix timestamp when the transaction was executed.
    Instant getExecutedAt();

    /// The type of transaction
    TransactionType getType();

    /// The currency in which the transaction was made.
    String getPriceCurrency();

    /// The amount of the transaction.
    BigDecimal getPriceAmount();

    /// The currency that was sent in the transaction.
    String getSentCurrency();

    /// The amount that was sent in the transaction.
    BigDecimal getSentAmount();

    /// The currency that was received in the transaction.
    String getReceivedCurrency();

    /// The amount that was received in the transaction.
    BigDecimal getReceivedAmount();

    /// The currency in which the fees were paid.
    String getFeesCurrency();

    /// The amount of fees paid in the transaction.
    BigDecimal getFeesAmount();

    /// The address where the transaction was made.
    String getAddress();
}
