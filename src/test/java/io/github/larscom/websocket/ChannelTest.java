package io.github.larscom.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.larscom.internal.ObjectMapperProvider;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ChannelTest {

    private final ObjectMapper objectMapper = ObjectMapperProvider.getObjectMapper();

    @Test
    void testSerialize() throws JsonProcessingException {
        final var channel = Channel.builder()
            .name(ChannelName.TICKER)
            .markets(Set.of("ETH-EUR"))
            .build();

        final var json = objectMapper.writeValueAsString(channel);
        assertThat(json).isEqualTo("{\"name\":\"ticker\",\"markets\":[\"ETH-EUR\"]}");
    }

    @Test
    void testDeserialize() throws JsonProcessingException {
        final var json = "{\"name\":\"ticker\",\"markets\":[\"ETH-EUR\"]}";
        final var deserialized = objectMapper.readValue(json, Channel.class);

        assertThat(deserialized.getName()).isEqualTo(ChannelName.TICKER);
        assertThat(deserialized.getMarkets()).isEqualTo(Set.of("ETH-EUR"));
        assertThat(deserialized.getIntervals())
            .isInstanceOf(Optional.class)
            .isEmpty();
    }

    @Test
    void testEquals() {
        final var channel1 = Channel.builder().name(ChannelName.TICKER).markets(Set.of("ETH-EUR", "ETH-USD")).build();
        final var channel2 = Channel.builder().name(ChannelName.TICKER).markets(Set.of("ETH-EUR")).build();

        assertThat(channel1).isEqualTo(channel2);
    }

    @Test
    void testHashCode() {
        final var channel1 = Channel.builder().name(ChannelName.TICKER).markets(Set.of("ETH-EUR", "ETH-USD")).build();
        final var channel2 = Channel.builder().name(ChannelName.TICKER).markets(Set.of("ETH-EUR")).build();

        assertThat(channel1).hasSameHashCodeAs(channel2);
    }
}