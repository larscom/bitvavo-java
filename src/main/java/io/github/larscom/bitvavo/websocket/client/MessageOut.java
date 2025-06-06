package io.github.larscom.bitvavo.websocket.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.larscom.bitvavo.websocket.channel.Channel;
import org.immutables.value.Value;

import java.util.Optional;
import java.util.Set;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonSerialize(as = ImmutableMessageOut.class)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableMessageOut.class)
@JsonIgnoreProperties(ignoreUnknown = true)
interface MessageOut {
    Action getAction();

    Optional<Set<Channel>> getChannels();

    /// API key
    Optional<String> getKey();

    /// SHA256 HMAC hex digest of timestamp + method + url + body
    Optional<String> getSignature();

    /// The current timestamp in milliseconds since 1 Jan 1970
    Optional<Long> getTimestamp();

    @Value.Check
    default void check() {
        getChannels().ifPresent(channels -> {
            if (channels.isEmpty()) {
                throw new IllegalStateException("Cannot build MessageOut, some of the attributes are empty [channels]");
            }
        });

        if (getAction() == Action.AUTHENTICATE) {
            if (getKey().isEmpty() || getSignature().isEmpty() || getTimestamp().isEmpty()) {
                throw new IllegalStateException("Cannot build MessageOut, some of the required attributes are missing [key, signature, timestamp]");
            }
        }
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableMessageOut.Builder {
    }
}
