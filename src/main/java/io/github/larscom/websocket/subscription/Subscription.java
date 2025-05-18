package io.github.larscom.websocket.subscription;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.larscom.websocket.ChannelName;
import io.github.larscom.websocket.MessageIn;
import org.immutables.value.Value;

import java.util.HashMap;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableSubscription.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Subscription extends MessageIn {

    @JsonProperty("subscriptions")
    HashMap<ChannelName, SubscriptionValue> getActiveSubscriptions();

}
