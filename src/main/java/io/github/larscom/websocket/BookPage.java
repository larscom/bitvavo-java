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
@JsonDeserialize(as = ImmutableBookPage.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface BookPage extends MessageIn {
    /// Bid / ask price.
    BigDecimal getPrice();

    /// Total order size on the price level, if the size is None it means that orders are no longer present at the price level.
    Optional<BigDecimal> getSize();
}
