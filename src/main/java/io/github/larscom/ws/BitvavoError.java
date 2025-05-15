package io.github.larscom.ws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableTicker.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface BitvavoError {
    int getErrorCode();

    @JsonProperty("error")
    String getErrorMessage();

    Optional<String> getAction();
}


