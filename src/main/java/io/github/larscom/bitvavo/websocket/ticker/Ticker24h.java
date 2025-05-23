package io.github.larscom.bitvavo.websocket.ticker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.larscom.bitvavo.websocket.MessageIn;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableTicker24h.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Ticker24h extends MessageIn {
    /// The ticker24h data.
    List<Ticker24hData> getData();
}
