package io.github.larscom.websocket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableError.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Error {
    /// Complete list of error codes: https://docs.bitvavo.com/docs/errors/
    int getErrorCode();

    /// A descriptive error message
    @JsonProperty("error")
    String getErrorMessage();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableError.Builder {
    }
}


