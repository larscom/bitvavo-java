package io.github.larscom.bitvavo.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.larscom.bitvavo.http.market.Market;
import io.github.larscom.bitvavo.internal.JsonBodyHandler;
import io.github.larscom.bitvavo.internal.ObjectMapperProvider;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
        final var request = HttpRequest.newBuilder()
            .uri(getURI("time"))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body))
            .map(objectMapper::readTree)
            .map(node -> node.get("time").asLong()));
    }

    public Single<List<Market>> getMarkets() {
        final var request = HttpRequest.newBuilder()
            .uri(getURI("markets"))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(request, new TypeReference<>() {})));
    }

    private static URI getURI(final String path) {
        try {
            return path.startsWith("/") ? new URI(String.format("%s%s", BASE_URL, path)) : new URI(String.format("%s/%s", BASE_URL, path));
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> Single<T> withIOScheduler(final Single<T> single) {
        return single.subscribeOn(Schedulers.io());
    }

    private <T> CompletableFuture<T> sendAsync(final HttpRequest request, final Class<T> type) {
        return httpClient.sendAsync(request, new JsonBodyHandler<>(type, objectMapper)).thenApply(HttpResponse::body);
    }

    private <T> CompletableFuture<T> sendAsync(final HttpRequest request, final TypeReference<T> type) {
        return httpClient.sendAsync(request, new JsonBodyHandler<>(type, objectMapper)).thenApply(HttpResponse::body);
    }
}
