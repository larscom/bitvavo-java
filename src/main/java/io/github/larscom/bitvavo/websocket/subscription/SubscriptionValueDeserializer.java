package io.github.larscom.bitvavo.websocket.subscription;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.larscom.bitvavo.websocket.candle.Interval;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class SubscriptionValueDeserializer extends JsonDeserializer<SubscriptionValue> {

    @Override
    public SubscriptionValue deserialize(final JsonParser p, final DeserializationContext ctx) throws IOException {
        final ObjectCodec codec = p.getCodec();
        final JsonNode node = codec.readTree(p);

        if (node.isArray()) {
            final Set<String> markets = codec.treeToValue(node, Set.class);
            return ImmutableSubscriptionWithMarkets.builder()
                .markets(markets)
                .build();
        }

        if (node.isObject()) {
            final Map<String, ArrayList<String>> map = codec.treeToValue(node, Map.class);
            final Map<Interval, Set<String>> intervalMap = map
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                    entry -> Interval.deserialize(entry.getKey()),
                    entry -> new HashSet<>(entry.getValue())
                ));

            return ImmutableSubscriptionWithInterval.builder()
                .intervalWithMarkets(intervalMap)
                .build();
        }

        throw new JsonParseException(p, "Unexpected JSON type for SubscriptionValue");
    }
}