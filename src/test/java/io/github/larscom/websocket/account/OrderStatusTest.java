package io.github.larscom.websocket.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.larscom.internal.ObjectMapperProvider;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderStatusTest {

    private final ObjectMapper objectMapper = ObjectMapperProvider.getObjectMapper();

    @Test
    void testSerialize() throws JsonProcessingException {
        final var json = objectMapper.writeValueAsString(OrderStatus.CANCELED_SELF_TRADE_PREVENTION);
        assertThat(json).isEqualTo("\"canceledSelfTradePrevention\"");
    }

    @Test
    void testDeserialize() throws JsonProcessingException {
        final var json = "\"canceledSelfTradePrevention\"";
        final var deserialized = objectMapper.readValue(json, OrderStatus.class);
        assertThat(deserialized).isEqualTo(OrderStatus.CANCELED_SELF_TRADE_PREVENTION);
    }
}
