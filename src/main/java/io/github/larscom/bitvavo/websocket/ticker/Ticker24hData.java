package io.github.larscom.bitvavo.websocket.ticker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableTicker24hData.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Ticker24hData {
    /// The market for this ticker24h (e.g. "ETH-EUR").
    String getMarket();

    /// The open price of the 24-hour period.
    BigDecimal getOpen();

    /// The highest price for which a trade occurred in the 24-hour period.
    BigDecimal getHigh();

    /// The lowest price for which a trade occurred in the 24-hour period.
    BigDecimal getLow();

    /// The last price for which a trade occurred in the 24-hour period.
    BigDecimal getLast();

    /// The total volume of the 24-hour period in base currency.
    BigDecimal getVolume();

    /// The total volume of the 24-hour period in quote currency.
    BigDecimal getVolumeQuote();

    /// The best (highest) bid offer at the current moment.
    BigDecimal getBid();

    /// The size of the best (highest) bid offer.
    BigDecimal getBidSize();

    /// The best (lowest) ask offer at the current moment.
    BigDecimal getAsk();

    /// The size of the best (lowest) ask offer.
    BigDecimal getAskSize();

    /// Timestamp in unix milliseconds.
    Long getTimestamp();
}
