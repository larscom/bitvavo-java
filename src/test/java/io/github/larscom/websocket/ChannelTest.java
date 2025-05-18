package io.github.larscom.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.internal.ObjectMapperProvider;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ChannelTest {

    @Test
    void testSerializeDeserialize() throws JsonProcessingException {
        final var objectMapper = ObjectMapperProvider.getObjectMapper();

        final var channel = Channel.builder()
            .name(ChannelName.TICKER)
            .markets(Set.of("ETH-EUR"))
            .build();

        final var json = objectMapper.writeValueAsString(channel);
        assertThat(json).isEqualTo("{\"name\":\"ticker\",\"markets\":[\"ETH-EUR\"]}");

        final var deserialized = objectMapper.readValue(json, Channel.class);

        assertThat(deserialized.getName()).isEqualTo(ChannelName.TICKER);
        assertThat(deserialized.getMarkets()).isEqualTo(Set.of("ETH-EUR"));
        assertThat(deserialized.getIntervals())
            .isInstanceOf(Optional.class)
            .isEmpty();
    }
}