package io.github.larscom.bitvavo.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public final class ObjectMapperProvider {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new Jdk8Module());
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}
