package io.github.larscom.bitvavo.websocket.subscription;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.util.Set;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableSubscriptionWithMarkets.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("immutables")
public interface SubscriptionWithMarkets extends SubscriptionValue {
    Set<String> getMarkets();
}
