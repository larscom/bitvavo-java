package io.github.larscom.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.larscom.Jackson;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class IntervalTest {

    @Test
    void testSerializeDeserialize() throws JsonProcessingException {
        final var objectMapper = Jackson.getObjectMapper();

        final var interval = Interval.H1;

        final var json = objectMapper.writeValueAsString(interval);
        assertThat(json).isEqualTo("\"1h\"");

        final var deserialized = objectMapper.readValue(json, Interval.class);

        assertThat(deserialized).isEqualTo(interval);
    }
}