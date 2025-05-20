package io.github.larscom.websocket.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.internal.ObjectMapperProvider;
import io.github.larscom.websocket.MessageInEvent;
import io.github.larscom.websocket.Side;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderTest {

    @Test
    void testDeserialize() throws JsonProcessingException {
        final var objectMapper = ObjectMapperProvider.getObjectMapper();

        final var json = "{\"event\":\"order\",\"orderId\":\"00000000-0000-047b-0100-0000cde63bcd\",\"market\":\"ETH-EUR\",\"guid\":\"d7095f10-7809-4bcd-99f1-4a10bf7c4d0e\",\"created\":1747680322356,\"updated\":1747680322356,\"status\":\"new\",\"side\":\"sell\",\"orderType\":\"limit\",\"amount\":\"0.1\",\"amountRemaining\":\"0.1\",\"price\":\"5224.2\",\"onHold\":\"0.1\",\"onHoldCurrency\":\"ETH\",\"selfTradePrevention\":\"decrementAndCancel\",\"visible\":true,\"timeInForce\":\"GTC\",\"postOnly\":false}";

        final var deserialized = objectMapper.readValue(json, Order.class);

        assertThat(deserialized.getEvent()).isEqualTo(MessageInEvent.ORDER);
        assertThat(deserialized.getMarket()).isEqualTo("ETH-EUR");
        assertThat(deserialized.getOrderId()).isEqualTo(UUID.fromString("00000000-0000-047b-0100-0000cde63bcd"));
        assertThat(deserialized.created()).isEqualTo(1747680322356L);
        assertThat(deserialized.updated()).isEqualTo(1747680322356L);
        assertThat(deserialized.getStatus()).isEqualTo(OrderStatus.NEW);
        assertThat(deserialized.getSide()).isEqualTo(Side.SELL);
        assertThat(deserialized.getOrderType()).isEqualTo(OrderType.LIMIT);
        assertThat(deserialized.getAmount()).isEqualByComparingTo("0.1");
        assertThat(deserialized.getAmountRemaining()).isEqualByComparingTo("0.1");
        assertThat(deserialized.getPrice()).isEqualByComparingTo("5224.2");
        assertThat(deserialized.getOnHold()).isEqualByComparingTo("0.1");
        assertThat(deserialized.getOnHoldCurrency()).isEqualTo("ETH");
        assertThat(deserialized.getSelfTradePrevention()).isEqualTo(SelfTradePrevention.DECREMENT_AND_CANCEL);
        assertThat(deserialized.getVisible()).isTrue();
        assertThat(deserialized.getTimeInForce()).hasValue(TimeInForce.GTC);
        assertThat(deserialized.getPostOnly()).isFalse();

        assertThat(deserialized.getTriggerPrice()).isEmpty();
        assertThat(deserialized.getTriggerAmount()).isEmpty();
        assertThat(deserialized.getTriggerType()).isEmpty();
        assertThat(deserialized.getTriggerReference()).isEmpty();
    }
}
