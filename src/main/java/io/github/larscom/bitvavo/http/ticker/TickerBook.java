package io.github.larscom.bitvavo.http.ticker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.util.Optional;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableTickerBook.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface TickerBook {
    /// The market for which you requested the current best bid and ask information (e.g. "ETH-EUR").
    String getMarket();

    /// The highest buy order in the quote currency for the market currently available on Bitvavo.
    Optional<BigDecimal> getBid();

    /// The amount of base currency for the bid in the order.
    Optional<BigDecimal> getBidSize();

    /// The lowest sell order in the quote currency for the market currently available on Bitvavo.
    Optional<BigDecimal> getAsk();

    /// The amount of base currency for the ask in the order.
    Optional<BigDecimal> getAskSize();
}
