package io.github.larscom.bitvavo.http.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableBalance.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Balance {
    /// The asset for which the balance was returned. (e.g: "ETH")
    String getSymbol();

    /// The balance what is available for use.
    BigDecimal getAvailable();

    /// The balance that is currently reserved for open orders.
    BigDecimal getInOrder();
}
