package io.github.larscom.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.larscom.internal.JsonBodyHandler;
import io.github.larscom.internal.ObjectMapperProvider;
import io.reactivex.rxjava3.core.Single;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class ReactiveApiClient {
    private static final String BASE_URL = "https://api.bitvavo.com/v2";
    private static final ObjectMapper objectMapper = ObjectMapperProvider.getObjectMapper();

    private final HttpClient httpClient;

    public ReactiveApiClient() {
        httpClient = HttpClient
            .newBuilder()
            .executor(Executors.newVirtualThreadPerTaskExecutor())
            .version(HttpClient.Version.HTTP_2)
            .build();
    }

    public Single<Long> getTime() {
        try {
            final var request = HttpRequest.newBuilder()
                .uri(new URI(String.format("%s/%s", BASE_URL, "time")))
                .GET()
                .build();

            return Single.fromFuture(httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body))
                .map(objectMapper::readTree)
                .map(node -> node.get("time").asLong());

        } catch (final URISyntaxException e) {
            return Single.error(e);
        }
    }

    public Single<List<Market>> getMarkets() {
        try {
            final var request = HttpRequest.newBuilder()
                .uri(new URI(String.format("%s/%s", BASE_URL, "markets")))
                .GET()
                .build();

            return Single.fromFuture(sendAsync(request, new TypeReference<>() {
            }));
        } catch (final URISyntaxException e) {
            return Single.error(e);
        }
    }

    private <T> CompletableFuture<T> sendAsync(final HttpRequest request, final Class<T> type) {
        return httpClient.sendAsync(request, new JsonBodyHandler<>(type, objectMapper)).thenApply(HttpResponse::body);
    }

    private <T> CompletableFuture<T> sendAsync(final HttpRequest request, final TypeReference<T> type) {
        return httpClient.sendAsync(request, new JsonBodyHandler<>(type, objectMapper)).thenApply(HttpResponse::body);
    }
}
