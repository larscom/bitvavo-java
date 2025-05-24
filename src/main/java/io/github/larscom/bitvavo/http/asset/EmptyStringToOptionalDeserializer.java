package io.github.larscom.bitvavo.http.asset;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Optional;

public class EmptyStringToOptionalDeserializer extends JsonDeserializer<Optional<String>> {

    @Override
    public Optional<String> deserialize(final JsonParser p, final DeserializationContext c) throws IOException {
        final String value = p.getValueAsString();
        if (value == null || value.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(value);
    }
}