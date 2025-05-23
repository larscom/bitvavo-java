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
@JsonDeserialize(as = ImmutableOrder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Order extends MessageIn {
    /// The market for this order (e.g. "ETH-EUR").
    String getMarket();

    /// The order ID
    UUID getOrderId();

    /// The personalized UUID you assigned to an order. This is only returned if you specified clientOrderId in the request.
    Optional<UUID> getClientOrderId();

    /// Created timestamp in unix milliseconds.
    long created();

    /// Updated timestamp in unix milliseconds.
    long updated();

    /// The status of the order.
    OrderStatus getStatus();

    /// Buy order or sell order.
    Side getSide();

    /// The type of the order (e.g. limit order).
    OrderType getOrderType();

    /// The original amount.
    BigDecimal getAmount();

    /// The remaining amount (lower than 'amount' after fills).
    BigDecimal getAmountRemaining();

    /// The price of the order.
    BigDecimal getPrice();

    /// Amount of 'onHoldCurrency' that is reserved for this order.
    /// This is released when orders are canceled.
    BigDecimal getOnHold();

    /// The currency placed on hold is the quote currency for sale orders and base
    /// currency for buy orders. (e.g: ETH)
    String getOnHoldCurrency();

    /// Only for stop orders: The current price used in the trigger.
    /// This is based on the triggerAmount and triggerType.
    Optional<BigDecimal> getTriggerPrice();

    /// Only for stop orders: The value used for the triggerType to determine the triggerPrice.
    Optional<BigDecimal> getTriggerAmount();

    /// Only for stop orders.
    Optional<OrderTriggerType> getTriggerType();

    /// Only for stop orders: The reference price used for stop orders.
    Optional<OrderTriggerRef> getTriggerReference();

    /// Only for limit orders: Determines how long orders remain active.
    /// GTC orders will remain on the order book until they are filled or canceled.
    /// IOC orders will fill against existing orders, but will cancel any remaining amount after that.
    /// FOK orders will fill against existing orders in its entirety, or will be canceled (if the entire order cannot be filled).
    Optional<TimeInForce> getTimeInForce();

    /// Only for limit orders: When postOnly is set to true, the order will not fill against existing orders.
    /// This is useful if you want to ensure you pay the maker fee.
    /// If the order would fill against existing orders, the entire order will be canceled.
    boolean getPostOnly();

    /// Self trading is not allowed on Bitvavo. Multiple options are available to prevent this from happening.
    /// The default ‘decrementAndCancel’ decrements both orders by the amount that would have been filled, which in turn cancels the smallest of the two orders.
    /// ‘cancelOldest’ will cancel the entire older order and places the new order.
    /// ‘cancelNewest’ will cancel the order that is submitted.
    /// ‘cancelBoth’ will cancel both the current and the old order.
    SelfTradePrevention getSelfTradePrevention();

    /// Whether this order is visible on the order book.
    boolean getVisible();
}
