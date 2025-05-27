package io.github.larscom.bitvavo.http.trade;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.immutables.value.Value;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
public interface TradeParams {
    /// Possible values: >= 1 and <= 1000
    ///
    /// The maximum number of trades to return.
    ///
    /// Default value: 500
    Optional<Integer> getLimit();

    /// Instant starting from which to return trades.
    /// NOTE: start and end must both be filled.
    Optional<Instant> getStart();

    /// Instant until which to return trades.
    /// NOTE: start and end must both be filled.
    ///
    /// Cannot be more than 24 hours after the start time.
    Optional<Instant> getEnd();

    /// The unique identifier of the trade starting from which to return the trades.
    Optional<UUID> getTradeIdFrom();

    /// The unique identifier of the trade up to which to return the trades.
    Optional<UUID> getTradeIdTo();

    default NameValuePair[] getPairs() {
        final var pairs = new ArrayList<NameValuePair>();

        getLimit().ifPresent(limit ->
            pairs.add(new BasicNameValuePair("limit", limit.toString()))
        );

        getStart().ifPresent(start ->
            pairs.add(new BasicNameValuePair("start", String.valueOf(start.toEpochMilli())))
        );

        getEnd().ifPresent(end ->
            pairs.add(new BasicNameValuePair("end", String.valueOf(end.toEpochMilli())))
        );

        getTradeIdFrom().ifPresent(tradeId ->
            pairs.add(new BasicNameValuePair("tradeIdFrom", tradeId.toString()))
        );

        getTradeIdTo().ifPresent(tradeId ->
            pairs.add(new BasicNameValuePair("tradeIdTo", tradeId.toString()))
        );

        return pairs.toArray(NameValuePair[]::new);
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableTradeParams.Builder {
    }
}
