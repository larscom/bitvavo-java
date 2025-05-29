package io.github.larscom.bitvavo.http.market;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.larscom.bitvavo.account.OrderType;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.util.Set;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableMarket.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Market {
    /// The market name (e.g. "ETH-EUR").
    String getMarket();

    /// The status of the market.
    MarketStatus getStatus();

    /// The base currency of the specified market (e.g. "ETH").
    String getBase();

    /// The quote currency of the specified market (e.g. "EUR").
    String getQuote();

    /// The number of digits allowed in the price when the order was created.
    Integer getPricePrecision();

    /// The minimum amount of base currency for an order in the specified market.
    BigDecimal getMinOrderInBaseAsset();

    /// The minimum amount of quote currency for an order in the specified market.
    BigDecimal getMinOrderInQuoteAsset();

    /// The maximum amount of base currency for an order in the specified market.
    BigDecimal getMaxOrderInBaseAsset();

    /// The maximum amount of quote currency for an order in the specified market.
    BigDecimal getMaxOrderInQuoteAsset();

    ///  The types of orders that can be placed in the specified market.
    Set<OrderType> getOrderTypes();
}
