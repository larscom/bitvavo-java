package io.github.larscom.websocket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableTrade.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Trade extends MessageIn {
    /// The market for this ticker (e.g. "ETH-EUR").
    String getMarket();

    /// The trade ID of the returned trade.
    UUID getId();

    /// The side for the taker.
    Side getSide();

    /// The amount in base currency for which the trade has been made.
    BigDecimal getAmount();

    /// The price in quote currency for which the trade has been made.
    BigDecimal getPrice();

    /// Timestamp in unix milliseconds.
    Long getTimestamp();
}
