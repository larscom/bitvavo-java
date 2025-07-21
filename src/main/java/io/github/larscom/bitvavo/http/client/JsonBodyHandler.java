package io.github.larscom.bitvavo.http.client;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.larscom.bitvavo.error.BitvavoError;
import io.github.larscom.bitvavo.error.BitvavoException;
import io.reactivex.rxjava3.annotations.NonNull;

import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodySubscriber;
import java.net.http.HttpResponse.BodySubscribers;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

public class JsonBodyHandler<T> implements BodyHandler<T> {

    private final Optional<Class<T>> clazz;
    private final Optional<TypeReference<T>> typeReference;

    private final ObjectMapper objectMapper;

    public JsonBodyHandler(@NonNull final Class<T> clazz,
                           @NonNull final ObjectMapper objectMapper) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(objectMapper);
        this.clazz = Optional.of(clazz);
        this.typeReference = Optional.empty();
        this.objectMapper = objectMapper;
    }

    public JsonBodyHandler(@NonNull final TypeReference<T> typeReference,
                           @NonNull final ObjectMapper objectMapper) {
        Objects.requireNonNull(typeReference);
        Objects.requireNonNull(objectMapper);
        this.clazz = Optional.empty();
        this.typeReference = Optional.of(typeReference);
        this.objectMapper = objectMapper;
    }

    @Override
    public BodySubscriber<T> apply(final HttpResponse.ResponseInfo responseInfo) {
        final BodySubscriber<String> upstream = BodySubscribers.ofString(StandardCharsets.UTF_8);
        return BodySubscribers.mapping(upstream, this::parse);
    }

    private T parse(final String body) {
        try {
            final var isError = objectMapper.readTree(body).has("error");
            if (isError) {
                throw new BitvavoException(objectMapper.readValue(body, BitvavoError.class));
            }

            if (clazz.isPresent()) {
                return objectMapper.readValue(body, clazz.get());
            } else if (typeReference.isPresent()) {
                return objectMapper.readValue(body, typeReference.get());
            } else {
                throw new IllegalStateException("TypeReference<T> and Class<T> are missing");
            }
        } catch (final JacksonException | IllegalStateException e) {
            throw new RuntimeException("Failed to deserialize into JSON", e);
        }
    }
}