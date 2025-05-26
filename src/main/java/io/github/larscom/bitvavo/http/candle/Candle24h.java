package io.github.larscom.bitvavo.http.candle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.util.Optional;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableCandle24h.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Candle24h {
    /// The market for which you requested the ticker data (e.g. "ETH-EUR").
    String getMarket();

    /// The time when the data was requested.
    Long getTimestamp();

    /// The time starting from which to return the data.
    Optional<Long> getStartTimestamp();

    /// The opening price for the market in the base currency.
    Optional<BigDecimal> getOpen();

    /// The time when the market opened after the startTimestamp.
    Optional<Long> getOpenTimestamp();

    /// The highest trade price for 1 unit of base currency in the quote currency.
    Optional<BigDecimal> getHigh();

    /// The lowest trade price for 1 unit of base currency in the quote currency.
    Optional<BigDecimal> getLow();

    /// The latest trade price for 1 unit of base currency in the quote currency.
    Optional<BigDecimal> getLast();

    /// The time when the market closed after the startTimestamp.
    Optional<Long> getCloseTimestamp();

    /// The highest buy order in the quote currency.
    Optional<BigDecimal> getBid();

    /// The amount of base currency for the bid.
    Optional<BigDecimal> getBidSize();

    /// The lowest sell order in the quote currency.
    Optional<BigDecimal> getAsk();

    /// The amount of base currency for the ask.
    Optional<BigDecimal> getAskSize();

    /// The total amount of base currency traded.
    Optional<BigDecimal> getVolume();

    /// The total amount of quote currency traded.
    Optional<BigDecimal> getVolumeQuote();
}
