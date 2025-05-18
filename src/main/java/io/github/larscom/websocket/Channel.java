package io.github.larscom.websocket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.Optional;
import java.util.Set;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonSerialize(as = ImmutableChannel.class)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableChannel.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Channel {

    ChannelName getName();

    Set<String> getMarkets();

    @JsonProperty("interval")
    Optional<Set<Interval>> getIntervals();

    @Value.Check
    default void check() {
        if (getMarkets().isEmpty()) {
            throw new IllegalStateException("Cannot build Channel, some of the attributes are empty [markets]");
        }
        getIntervals().ifPresent(intervals -> {
            if (intervals.isEmpty()) {
                throw new IllegalStateException("Cannot build Channel, some of the attributes are empty [intervals]");
            }
        });
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableChannel.Builder {
    }
}
