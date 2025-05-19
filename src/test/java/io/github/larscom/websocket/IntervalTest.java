package io.github.larscom.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.larscom.internal.ObjectMapperProvider;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class IntervalTest {

    private final ObjectMapper objectMapper = ObjectMapperProvider.getObjectMapper();

    @Test
    void testSerialize() throws JsonProcessingException {
        final var json = objectMapper.writeValueAsString(Interval.H1);
        assertThat(json).isEqualTo("\"1h\"");
    }

    @Test
    void testDeserialize() throws JsonProcessingException {
        final var json = "\"1h\"";
        final var deserialized = objectMapper.readValue(json, Interval.class);

        assertThat(deserialized).isEqualTo(Interval.H1);
    }
}
