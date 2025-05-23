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
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class ReactiveApiClient {
    private static final String BASE_URL = "https://api.bitvavo.com/v2";
    private static final ObjectMapper objectMapper = ObjectMapperProvider.getObjectMapper();

    private static final String HEADER_RATE_LIMIT_LIMIT = "Bitvavo-Ratelimit-Limit";
    private static final String HEADER_RATE_LIMIT_REMAINING = "Bitvavo-Ratelimit-Remaining";
    private static final String HEADER_RATE_LIMIT_RESET_AT = "Bitvavo-Ratelimit-Resetat";

    private static final String HEADER_ACCESS_KEY = "Bitvavo-Access-Key";
    private static final String HEADER_ACCESS_SIGNATURE = "Bitvavo-Access-Signature";
    private static final String HEADER_ACCESS_TIMESTAMP = "Bitvavo-Access-Timestamp";
    private static final String HEADER_ACCESS_WINDOW = "Bitvavo-Access-Window";

    private volatile BitvavoRateLimit rateLimit = BitvavoRateLimit.DEFAULT;

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

    public BitvavoRateLimit getRateLimit() {
        return rateLimit;
    }

    public Single<Long> getTime() {
        final var request = HttpRequest.newBuilder()
            .uri(getURI("time"))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(withRateLimit(httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())))
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

    private <T> CompletableFuture<T> withRateLimit(final CompletableFuture<HttpResponse<T>> response) {
        return response.thenApply(r -> {
            updateRateLimit(r.headers());
            return r.body();
        });
    }

    private static <T> Single<T> withIOScheduler(final Single<T> single) {
        return single.subscribeOn(Schedulers.io());
    }

    private <T> CompletableFuture<T> sendAsync(final HttpRequest request, final Class<T> type) {
        return withRateLimit(httpClient.sendAsync(request, new JsonBodyHandler<T>(type, objectMapper)));
    }

    private <T> CompletableFuture<T> sendAsync(final HttpRequest request, final TypeReference<T> type) {
        return withRateLimit(httpClient.sendAsync(request, new JsonBodyHandler<>(type, objectMapper)));
    }

    private void updateRateLimit(final HttpHeaders headers) {
        final var limit = headers.firstValue(HEADER_RATE_LIMIT_LIMIT).map(Integer::parseInt);
        final var remaining = headers.firstValue(HEADER_RATE_LIMIT_REMAINING).map(Integer::parseInt);
        final var resetAt = headers.firstValue(HEADER_RATE_LIMIT_RESET_AT).map(Long::parseLong);

        limit.flatMap(lim -> remaining.flatMap(rem -> resetAt.map(res -> BitvavoRateLimit.of(lim, rem, res))))
            .ifPresent(r -> rateLimit = r);
    }

    public static class BitvavoRateLimit {
        private final int limit;
        private final int remaining;
        private final Instant resetAt;

        public static BitvavoRateLimit DEFAULT = new BitvavoRateLimit(1000, 1000, Instant.now().toEpochMilli());

        private BitvavoRateLimit(final int limit, final int remaining, final long resetAt) {
            this.limit = limit;
            this.remaining = remaining;
            this.resetAt = Instant.ofEpochMilli(resetAt);
        }

        public static BitvavoRateLimit of(final int limit, final int remaining, final long resetAt) {
            return new BitvavoRateLimit(limit, remaining, resetAt);
        }

        public Instant getResetAt() {
            return resetAt;
        }

        public int getRemaining() {
            return remaining;
        }

        public int getLimit() {
            return limit;
        }

        @Override
        public String toString() {
            return "BitvavoRateLimit{" +
                "limit=" + limit +
                ", remaining=" + remaining +
                ", resetAt=" + resetAt +
                '}';
        }
    }
}
