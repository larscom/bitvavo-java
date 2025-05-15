package io.github.larscom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonSerialize(as = ImmutableMessageIn.class)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableMessageIn.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface MessageIn {

    MessageInEvent getEvent();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableMessageIn.Builder {
    }
}
