package io.github.larscom.bitvavo.http.account;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.immutables.value.Value;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
public interface TransactionHistoryParams {
    /// The Instant starting from which to return transactions.
    Optional<Instant> fromDate();

    /// The Instant up to which to return transactions.
    Optional<Instant> toDate();

    /// The page number for the transaction history (must be >= 1)
    Optional<Integer> getPage();

    /// The maximum number of items per page in the transaction history. (must be >= 1 and <= 100)
    Optional<Integer> getMaxItems();

    /// The type of transaction to return.
    Optional<TransactionType> getType();

    default NameValuePair[] getPairs() {
        final var pairs = new ArrayList<NameValuePair>();

        fromDate().ifPresent(start ->
            pairs.add(new BasicNameValuePair("fromDate", String.valueOf(start.toEpochMilli())))
        );

        toDate().ifPresent(end ->
            pairs.add(new BasicNameValuePair("toDate", String.valueOf(end.toEpochMilli())))
        );

        getPage().ifPresent(page ->
            pairs.add(new BasicNameValuePair("page", page.toString()))
        );

        getMaxItems().ifPresent(maxItems ->
            pairs.add(new BasicNameValuePair("maxItems", maxItems.toString()))
        );

        getType().ifPresent(type ->
            pairs.add(new BasicNameValuePair("type", type.name().toLowerCase()))
        );

        return pairs.toArray(NameValuePair[]::new);
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableTransactionHistoryParams.Builder {
    }
}
