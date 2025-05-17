package io.github.larscom.websocket;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;
import java.util.Map;

class SubscriptionValueDeserializer extends JsonDeserializer<SubscriptionValue> {


    @Override
    public SubscriptionValue deserialize(final JsonParser p, final DeserializationContext ctx) throws IOException {
        final ObjectCodec codec = p.getCodec();
        final JsonNode node = codec.readTree(p);

        if (node.isArray()) {
            final List<String> markets = codec.treeToValue(node, List.class);
            return ImmutableSubscriptionSimpleValue.builder().markets(markets).build();
        } else if (node.isObject()) {
            final Map<Interval, List<String>> intervalMap = codec.treeToValue(node, Map.class);
            return ImmutableSubscriptionIntervalValue.builder().intervalWithMarkets(intervalMap).build();
        } else {
            throw new JsonParseException(p, "Unexpected JSON type for Value");
        }
    }
}