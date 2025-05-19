package io.github.larscom.websocket.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.larscom.websocket.MessageIn;
import org.immutables.value.Value;

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

    /// Created timestamp in unix milliseconds.
    long created();

    /// Updated timestamp in unix milliseconds.
    long updated();

    /// The status of the order.
    OrderStatus getStatus();
}
