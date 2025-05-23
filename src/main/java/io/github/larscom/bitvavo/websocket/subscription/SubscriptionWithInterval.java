package io.github.larscom.bitvavo.websocket.subscription;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.larscom.bitvavo.websocket.candle.Interval;
import org.immutables.value.Value;

import java.util.Map;
import java.util.Set;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableSubscriptionWithInterval.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("immutables")
public interface SubscriptionWithInterval extends SubscriptionValue {
    Map<Interval, Set<String>> getIntervalWithMarkets();
}
