package io.github.larscom.websocket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.util.Map;
import java.util.Set;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableSubscriptionIntervalValue.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("immutables")
public interface SubscriptionIntervalValue extends SubscriptionValue {
    Map<Interval, Set<String>> getIntervalWithMarkets();
}
