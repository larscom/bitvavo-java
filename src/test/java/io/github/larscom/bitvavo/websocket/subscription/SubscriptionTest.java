package io.github.larscom.bitvavo.websocket.subscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.bitvavo.internal.ObjectMapperProvider;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SubscriptionTest {

    @Test
    void testDeserialize() throws JsonProcessingException {
        final var objectMapper = ObjectMapperProvider.getObjectMapper();
        final var json = """
            {
              "event" : "subscribed",
              "subscriptions" : {
                "candles" : {
                  "8h" : [ "ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR" ],
                  "2h" : [ "ETH-EUR", "BTC-EUR", "POLYX-EUR", "APT-EUR", "VANRY-EUR" ]
                }
              }
            }
            """;

        final var subscription = objectMapper.readValue(json, Subscription.class);
        assertThat(subscription.getActiveSubscriptions().isEmpty()).isFalse();
    }
}