package io.github.larscom.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.Jackson;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ChannelTest {

    @Test
    void testSerializeDeserialize() throws JsonProcessingException {
        final var objectMapper = Jackson.getObjectMapper();

        final var channel = Channel.builder()
            .name(ChannelName.TICKER)
            .markets(List.of("ETH-EUR"))
            .build();

        final var json = objectMapper.writeValueAsString(channel);
        assertThat(json).isEqualTo("{\"name\":\"ticker\",\"markets\":[\"ETH-EUR\"]}");

        final var deserialized = objectMapper.readValue(json, Channel.class);

        assertThat(deserialized.getName()).isEqualTo(ChannelName.TICKER);
        assertThat(deserialized.getMarkets()).isEqualTo(List.of("ETH-EUR"));
        assertThat(deserialized.getIntervals())
            .isInstanceOf(Optional.class)
            .isEmpty();
    }
}