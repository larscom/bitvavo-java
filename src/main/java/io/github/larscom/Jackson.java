package io.github.larscom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public final class Jackson {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new Jdk8Module());
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}
