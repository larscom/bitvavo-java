package io.github.larscom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import io.github.larscom.websocket.Channel;
import io.github.larscom.websocket.ChannelName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ChannelTest {

    @Test
    void testChannel() throws JsonProcessingException {
        final var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());

        final var channel = Channel.builder()
            .name(ChannelName.TICKER)
            .markets(List.of("ETH-EUR"))
            .build();

        final var json = objectMapper.writeValueAsString(channel);
        System.out.printf(json);

        assertThat(json).isEqualTo("{\"name\":\"ticker\",\"markets\":[\"ETH-EUR\"]}");

        final var deserialized = objectMapper.readValue(json, Channel.class);

        assertThat(deserialized.getName()).isEqualTo(ChannelName.TICKER);
        assertThat(deserialized.getMarkets()).isEqualTo(List.of("ETH-EUR"));
        assertThat(deserialized.getIntervals())
            .isInstanceOf(Optional.class)
            .isEmpty();
    }

}