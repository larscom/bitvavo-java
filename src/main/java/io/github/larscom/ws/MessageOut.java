package io.github.larscom.ws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonSerialize(as = ImmutableMessageOut.class)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableMessageOut.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface MessageOut {

    Action getAction();

    Optional<List<Channel>> getChannels();

    /**
     * API key
     */
    Optional<String> getKey();

    /**
     * SHA256 HMAC hex digest of timestamp + method + url + body
     */
    Optional<String> getSignature();

    /**
     * The current timestamp in milliseconds since 1 Jan 1970
     */
    Optional<Long> getTimestamp();

    @Value.Check
    default void check() {
        getChannels().ifPresent(channels -> {
            if (channels.isEmpty()) {
                throw new IllegalStateException("Cannot build MessageOut, some of required attributes are empty [channels]");
            }
        });
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableMessageOut.Builder {
    }
}
