package io.github.larscom.websocket.candle;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.larscom.websocket.MessageInEvent;

import java.io.IOException;
import java.math.BigDecimal;

class CandleDeserializer extends JsonDeserializer<Candle> {

    @Override
    public Candle deserialize(final JsonParser p, final DeserializationContext ctx) throws IOException {
        final ObjectNode node = p.getCodec().readTree(p);

        final MessageInEvent event = MessageInEvent.deserialize(node.get("event").asText());
        final String market = node.get("market").asText();
        final Interval interval = Interval.deserialize(node.get("interval").asText());

        final JsonNode candleArray = node.get("candle");
        if (candleArray == null || !candleArray.isArray() || candleArray.isEmpty()) {
            throw new JsonMappingException(p, "Missing or invalid 'candle' array");
        }

        final JsonNode candle = candleArray.get(0);
        if (candle.size() != 6) {
            throw new JsonMappingException(p, "Candle array must have 6 elements");
        }

        final Long timestamp = candle.get(0).asLong();
        final BigDecimal open = new BigDecimal(candle.get(1).asText());
        final BigDecimal high = new BigDecimal(candle.get(2).asText());
        final BigDecimal low = new BigDecimal(candle.get(3).asText());
        final BigDecimal close = new BigDecimal(candle.get(4).asText());
        final BigDecimal volume = new BigDecimal(candle.get(5).asText());

        return ImmutableCandle.builder()
            .event(event)
            .market(market)
            .interval(interval)
            .timestamp(timestamp)
            .open(open)
            .high(high)
            .low(low)
            .close(close)
            .volume(volume)
            .build();
    }
}