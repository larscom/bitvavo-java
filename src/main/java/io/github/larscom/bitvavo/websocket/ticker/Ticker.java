package io.github.larscom.bitvavo.websocket.ticker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.larscom.bitvavo.websocket.message.MessageIn;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.util.Optional;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableTicker.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Ticker extends MessageIn {
    /// The market for this ticker (e.g. "ETH-EUR").
    String getMarket();

    /// The price of the best (highest) bid offer available, only sent when either bestBid or bestBidSize has changed.
    Optional<BigDecimal> getBestBid();

    /// The size of the best (highest) bid offer available, only sent when either bestBid or bestBidSize has changed.
    Optional<BigDecimal> getBestBidSize();

    /// The price of the best (lowest) ask offer available, only sent when either bestAsk or bestAskSize has changed.
    Optional<BigDecimal> getBestAsk();

    /// The size of the best (lowest) ask offer available, only sent when either bestAsk or bestAskSize has changed.
    Optional<BigDecimal> getBestAskSize();

    /// The last price for which a trade has occurred, only sent when lastPrice has changed.
    Optional<BigDecimal> getLastPrice();
}
