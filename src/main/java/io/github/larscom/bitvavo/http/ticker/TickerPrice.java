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
@JsonDeserialize(as = ImmutableTickerPrice.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface TickerPrice {
    /// The market for which you requested the latest trade price. (e.g. "ETH-EUR").
    String getMarket();

    /// The latest trade price for 1 unit of base currency in the amount of quote currency for the specified market.
    /// For example, 34243 Euro.
    Optional<BigDecimal> getPrice();
}
