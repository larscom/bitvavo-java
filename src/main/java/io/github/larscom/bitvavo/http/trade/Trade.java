package io.github.larscom.bitvavo.http.trade;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.larscom.bitvavo.trade.Side;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableTrade.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Trade {
    /// The trade ID of the returned trade.
    UUID getId();

    /// The Unix timestamp when the trade was made.
    Long getTimestamp();

    /// The amount of base currency exchanged in the trade. For example, 0.1 Bitcoin.
    BigDecimal getAmount();

    /// The price of 1 unit of base currency in the amount of quote currency at the time of the trade. For example, 34243 Euro.
    BigDecimal getPrice();

    /// Indicates if the taker, who fills the order, is selling or buying.
    Side getSide();
}
