package io.github.larscom.bitvavo.http.market;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableMarket.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Market {
    /// The market name (e.g. "ETH-EUR").
    String getMarket();
}
