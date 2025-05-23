package io.github.larscom.bitvavo.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.larscom.bitvavo.http.market.Market;
import io.github.larscom.bitvavo.internal.JsonBodyHandler;
import io.github.larscom.bitvavo.internal.ObjectMapperProvider;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class ReactiveApiClient {
    private static final String BASE_URL = "https://api.bitvavo.com/v2";
    private static final ObjectMapper objectMapper = ObjectMapperProvider.getObjectMapper();

    private final HttpClient httpClient;

    public ReactiveApiClient(@NonNull final InetSocketAddress proxyAddress) {
        this(Optional.of(proxyAddress));
    }

    public ReactiveApiClient() {
        this(Optional.empty());
    }

    private ReactiveApiClient(final Optional<InetSocketAddress> proxyAddress) {
        final var builder = HttpClient.newBuilder().executor(Executors.newVirtualThreadPerTaskExecutor());
        proxyAddress.map(ProxySelector::of).ifPresent(builder::proxy);
        httpClient = builder.build();
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

    public Single<Market> getMarket(final String market) {
        final var request = HttpRequest.newBuilder()
            .uri(getURI("markets", createParameter("market", market)))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(request, Market.class)));
    }

    private static URI getURI(final String path, final NameValuePair... parameters) {
        final var url = path.startsWith("/") ? String.format("%s%s", BASE_URL, path) : String.format("%s/%s", BASE_URL, path);

        try {
            return new URIBuilder(url)
                .addParameters(Arrays.stream(parameters).toList())
                .build();
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static BasicNameValuePair createParameter(final String key, final String value) {
        return new BasicNameValuePair(key, value);
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
