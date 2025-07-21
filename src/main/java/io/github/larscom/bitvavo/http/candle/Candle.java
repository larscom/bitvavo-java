package io.github.larscom.bitvavo.http.candle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableCandle.class, using = CandleDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Candle {
    /// Timestamp in unix milliseconds.
    Long getTimestamp();

    /// The opening price at the start of the interval.
    BigDecimal getOpen();

    /// The highest price during the interval.
    BigDecimal getHigh();

    /// The lowest price during the interval.
    BigDecimal getLow();

    /// The closing price at the end of the interval.
    BigDecimal getClose();

    /// The trading volume during the interval.
    BigDecimal getVolume();
}
