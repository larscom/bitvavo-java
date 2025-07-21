package io.github.larscom.bitvavo.http.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableTransactionHistory.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface TransactionHistory {
    @JsonProperty("items")
    List<Transaction> getTransactions();

    /// The current page number.
    Integer getCurrentPage();

    /// The total number of returned pages.
    Integer getTotalPages();

    /// The maximum number of transactions per page.
    Integer getMaxItems();
}
