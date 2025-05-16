package io.github.larscom.websocket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.util.Optional;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableTicker.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Ticker extends MessageIn {
    String getMarket();

    Optional<BigDecimal> getBestBid();

    Optional<BigDecimal> getBestBidSize();

    Optional<BigDecimal> getBestAsk();

    Optional<BigDecimal> getBestAskSize();

    Optional<BigDecimal> getLastPrice();
}
