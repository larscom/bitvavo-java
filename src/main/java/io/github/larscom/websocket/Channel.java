package io.github.larscom.websocket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.larscom.websocket.candle.Interval;
import org.immutables.value.Value;

import java.util.Optional;
import java.util.Set;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonSerialize(as = ImmutableChannel.class)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableChannel.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Channel {

    public abstract ChannelName getName();

    public abstract Set<String> getMarkets();

    @JsonProperty("interval")
    public abstract Optional<Set<Interval>> getIntervals();

    @Value.Check
    protected void check() {
        if (getMarkets().isEmpty()) {
            throw new IllegalStateException("Cannot build Channel, some of the attributes are empty [markets]");
        }

        if (getName() == ChannelName.CANDLES) {
            getIntervals().ifPresent(intervals -> {
                if (intervals.isEmpty()) {
                    throw new IllegalStateException("Cannot build Channel, some of the attributes are empty [intervals]");
                }
            });
        } else {
            getIntervals().ifPresent(__ -> {
                throw new IllegalStateException("Cannot build Channel, intervals is only supported for CANDLES");
            });
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Channel channel)) return false;
        return getName() == channel.getName();
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ImmutableChannel.Builder {
    }
}

