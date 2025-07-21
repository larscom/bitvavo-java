package io.github.larscom.bitvavo.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableBitvavoError.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface BitvavoError {
    /// Complete list of error codes: https://docs.bitvavo.com/docs/errors/
    Integer getErrorCode();

    /// A descriptive error message
    @JsonProperty("error")
    String getErrorMessage();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableBitvavoError.Builder {
    }
}


