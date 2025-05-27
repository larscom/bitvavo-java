package io.github.larscom.bitvavo.http.candle;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.immutables.value.Value;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
public interface CandleParams {
    /// Possible values: >= 1 and <= 1440
    ///
    /// The maximum number of candles to return.
    ///
    /// Default value: 1440
    Optional<Integer> getLimit();

    /// The Instant starting from which to return the candlestick data.
    /// Candlestick data is always returned in a chronological order from latest to earliest.
    Optional<Instant> getStart();

    /// The Instant until which to return the candlestick data.
    /// Candlestick data is always returned in a chronological order from latest to earliest.
    /// We recommend using the end query parameter.
    Optional<Instant> getEnd();

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

        return pairs.toArray(NameValuePair[]::new);
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableCandleParams.Builder {
    }
}
