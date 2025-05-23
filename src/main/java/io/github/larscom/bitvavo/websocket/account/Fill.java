package io.github.larscom.bitvavo.websocket.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.larscom.bitvavo.websocket.MessageIn;
import io.github.larscom.bitvavo.websocket.Side;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableFill.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Fill extends MessageIn {
    /// The market for this fill (e.g. "ETH-EUR").
    String getMarket();

    /// The identifier of the order on which has been filled.
    UUID getOrderId();

    /// The identifier of the returned fill.
    UUID getFillId();

    /// The current timestamp in milliseconds since 1 Jan 1970.
    long getTimestamp();

    /// The amount in base currency for which the trade has been made.
    BigDecimal getAmount();

    /// Indicates if the order was to buy or sell an asset.
    Side getSide();

    /// The price in quote currency for which the trade has been made.
    BigDecimal getPrice();

    /// Indicates if you are the taker or maker in the trade.
    /// true: you are the taker. Your order filled a resting order.
    /// false: you are the maker. Your resting order was filled by an incoming order.
    boolean getTaker();

    /// The amount you pay Bitvavo in feeCurrency to process orderId.
    /// For more information about your fees, call GET /account/fees.
    /// If settled is false, this object is not included in these return parameters.
    ///  To retrieve the value of settled, see the fills object in the return parameters for calls to GET /order.
    /// fee is negative for rebates.
    Optional<BigDecimal> getFee();

    /// The currency that fee is paid in.
    /// If settled is false, this object is not included in these return parameters.
    /// To retrieve the value of settled, see the fills object in the return parameters for calls to GET /order.
    Optional<String> getFeeCurrency();
}
