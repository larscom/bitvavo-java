package io.github.larscom.bitvavo.websocket.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.larscom.bitvavo.websocket.message.MessageIn;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableAuthentication.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Authentication extends MessageIn {
    /// Whether the user is successfully authenticated.
    boolean getAuthenticated();
}
