package io.github.larscom.bitvavo.http.candle;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.math.BigDecimal;

class CandleDeserializer extends JsonDeserializer<Candle> {

    @Override
    public Candle deserialize(final JsonParser p, final DeserializationContext ctx) throws IOException {
        final ArrayNode candleArray = p.getCodec().readTree(p);
        if (candleArray.size() != 6) {
            throw new JsonMappingException(p, "Candle array must have 6 elements");
        }

        final Long timestamp = candleArray.get(0).asLong();
        final BigDecimal open = new BigDecimal(candleArray.get(1).asText());
        final BigDecimal high = new BigDecimal(candleArray.get(2).asText());
        final BigDecimal low = new BigDecimal(candleArray.get(3).asText());
        final BigDecimal close = new BigDecimal(candleArray.get(4).asText());
        final BigDecimal volume = new BigDecimal(candleArray.get(5).asText());

        return ImmutableCandle.builder()
            .timestamp(timestamp)
            .open(open)
            .high(high)
            .low(low)
            .close(close)
            .volume(volume)
            .build();
    }
}