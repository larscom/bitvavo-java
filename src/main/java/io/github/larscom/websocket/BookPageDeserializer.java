package io.github.larscom.websocket;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

public class BookPageDeserializer extends JsonDeserializer<BookPage> {

    @Override
    public BookPage deserialize(final JsonParser p, final DeserializationContext ctx) throws IOException {
        final JsonNode node = p.getCodec().readTree(p);
        if (!node.isArray() || node.size() != 2) {
            throw new JsonParseException(p, "Must be a 2-element array");
        }

        final BigDecimal price = new BigDecimal(node.get(0).asText());
        final String sizeStr = node.get(1).asText();
        final Optional<BigDecimal> size = sizeStr.equals("0") ? Optional.empty() : Optional.of(new BigDecimal(sizeStr));

        return ImmutableBookPage.builder()
            .price(price)
            .size(size)
            .build();
    }
}