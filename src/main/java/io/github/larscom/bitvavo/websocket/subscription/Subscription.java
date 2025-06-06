package io.github.larscom.bitvavo.websocket.subscription;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.larscom.bitvavo.websocket.channel.ChannelName;
import io.github.larscom.bitvavo.websocket.message.MessageIn;
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
