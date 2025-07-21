package io.github.larscom.bitvavo.http.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.larscom.bitvavo.account.Credentials;
import io.github.larscom.bitvavo.candle.Interval;
import io.github.larscom.bitvavo.crypto.CryptoUtils;
import io.github.larscom.bitvavo.http.account.*;
import io.github.larscom.bitvavo.http.asset.Asset;
import io.github.larscom.bitvavo.http.book.Book;
import io.github.larscom.bitvavo.http.candle.Candle;
import io.github.larscom.bitvavo.http.candle.Candle24h;
import io.github.larscom.bitvavo.http.candle.CandleParams;
import io.github.larscom.bitvavo.http.market.Market;
import io.github.larscom.bitvavo.http.ticker.TickerBook;
import io.github.larscom.bitvavo.http.ticker.TickerPrice;
import io.github.larscom.bitvavo.http.trade.Trade;
import io.github.larscom.bitvavo.http.trade.TradeParams;
import io.github.larscom.bitvavo.json.ObjectMapperProvider;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class ReactiveHttpClient implements PublicApi, PrivateApi {
    private static final int DEFAULT_WINDOW_ACCESS_TIME = 10_000;
    private static final String BASE_URL = "https://api.bitvavo.com/v2";
    private static final ObjectMapper objectMapper = ObjectMapperProvider.getObjectMapper();

    private static final String HEADER_RATE_LIMIT_LIMIT = "Bitvavo-Ratelimit-Limit";
    private static final String HEADER_RATE_LIMIT_REMAINING = "Bitvavo-Ratelimit-Remaining";
    private static final String HEADER_RATE_LIMIT_RESET_AT = "Bitvavo-Ratelimit-Resetat";

    private static final String HEADER_ACCESS_KEY = "Bitvavo-Access-Key";
    private static final String HEADER_ACCESS_SIGNATURE = "Bitvavo-Access-Signature";
    private static final String HEADER_ACCESS_TIMESTAMP = "Bitvavo-Access-Timestamp";
    private static final String HEADER_ACCESS_WINDOW = "Bitvavo-Access-Window";

    private final Optional<Credentials> credentials;
    private final int windowAccesTime;

    private final BehaviorSubject<RateLimitQuota> rateLimitQuota;
    private final HttpClient httpClient;

    public static PublicApi newPublic() {
        return new ReactiveHttpClient(Optional.empty(), Optional.empty(), DEFAULT_WINDOW_ACCESS_TIME);
    }

    public static PublicApi newPublic(@NonNull final InetSocketAddress proxyAddress) {
        return new ReactiveHttpClient(Optional.empty(), Optional.of(proxyAddress), DEFAULT_WINDOW_ACCESS_TIME);
    }

    public static PrivateApi newPrivate(@NonNull final Credentials credentials) {
        return new ReactiveHttpClient(Optional.of(credentials), Optional.empty(), DEFAULT_WINDOW_ACCESS_TIME);
    }

    public static PrivateApi newPrivate(@NonNull final Credentials credentials, final int windowAccesTime) {
        return new ReactiveHttpClient(Optional.of(credentials), Optional.empty(), windowAccesTime);
    }

    public static PrivateApi newPrivate(
        @NonNull final Credentials credentials,
        @NonNull final InetSocketAddress proxyAddress
    ) {
        return new ReactiveHttpClient(Optional.of(credentials), Optional.of(proxyAddress), DEFAULT_WINDOW_ACCESS_TIME);
    }

    public static PrivateApi newPrivate(
        @NonNull final Credentials credentials,
        @NonNull final InetSocketAddress proxyAddress,
        final int windowAccesTime) {
        return new ReactiveHttpClient(Optional.of(credentials), Optional.of(proxyAddress), windowAccesTime);
    }

    private ReactiveHttpClient(
        final Optional<Credentials> credentials,
        final Optional<InetSocketAddress> proxyAddress,
        final int windowAccesTime
    ) {
        this.windowAccesTime = windowAccesTime;
        this.credentials = credentials;

        rateLimitQuota = BehaviorSubject.createDefault(RateLimitQuota.DEFAULT);

        final var builder = HttpClient.newBuilder()
            .executor(Executors.newVirtualThreadPerTaskExecutor())
            .connectTimeout(Duration.ofSeconds(30));

        proxyAddress.map(ProxySelector::of).ifPresent(builder::proxy);

        httpClient = builder.build();
    }

    @Override
    public Observable<RateLimitQuota> getRateLimitQuota() {
        return Observable.wrap(rateLimitQuota);
    }

    @Override
    public RateLimitQuota getCurrentRateLimitQuota() {
        return rateLimitQuota.getValue();
    }

    @Override
    public Single<Long> getTime() {
        final var request = getRequestBuilder(getURI("time"))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(withRateLimit(httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())))
            .map(objectMapper::readTree)
            .map(node -> node.get("time").asLong()));
    }

    @Override
    public Single<List<Market>> getMarkets() {
        final var request = getRequestBuilder(getURI("markets"))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(request, new TypeReference<>() {})));
    }

    @Override
    public Single<Market> getMarket(final String market) {
        final var request = getRequestBuilder(getURI("markets", createParameter("market", market)))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(request, Market.class)));
    }

    @Override
    public Single<List<Asset>> getAssets() {
        final var request = getRequestBuilder(getURI("assets"))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(request, new TypeReference<>() {})));
    }

    @Override
    public Single<Asset> getAsset(final String symbol) {
        final var request = getRequestBuilder(getURI("assets", createParameter("symbol", symbol)))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(request, Asset.class)));
    }

    @Override
    public Single<Book> getOrderBook(final String market) {
        return getOrderBook(market, 1000);
    }

    @Override
    public Single<Book> getOrderBook(final String market, final int depth) {
        final var request = getRequestBuilder(getURI(String.format("%s/book", market), createParameter("depth", String.valueOf(depth))))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(request, Book.class)));
    }

    @Override
    public Single<List<Trade>> getTrades(final String market) {
        return getTrades(market, null);
    }

    @Override
    public Single<List<Trade>> getTrades(final String market, final TradeParams tradeParams) {
        final var params = Optional.ofNullable(tradeParams).map(TradeParams::getPairs)
            .orElseGet(() -> new NameValuePair[0]);

        final var request = getRequestBuilder(getURI(String.format("%s/trades", market), params))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(request, new TypeReference<>() {})));
    }

    @Override
    public Single<List<TickerPrice>> getTickerPrices() {
        final var request = getRequestBuilder(getURI("ticker/price"))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(request, new TypeReference<>() {})));
    }

    @Override
    public Single<TickerPrice> getTickerPrice(final String market) {
        final var request = getRequestBuilder(getURI("ticker/price", createParameter("market", market)))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(request, TickerPrice.class)));
    }

    @Override
    public Single<List<TickerBook>> getTickerBooks() {
        final var request = getRequestBuilder(getURI("ticker/book"))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(request, new TypeReference<>() {})));
    }

    @Override
    public Single<TickerBook> getTickerBook(final String market) {
        final var request = getRequestBuilder(getURI("ticker/book", createParameter("market", market)))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(request, TickerBook.class)));
    }

    @Override
    public Single<List<Candle>> getCandles(final String market, final Interval interval) {
        return getCandles(market, interval, null);
    }

    @Override
    public Single<List<Candle>> getCandles(final String market, final Interval interval, final CandleParams candleParams) {
        final var params = Optional.ofNullable(candleParams).map(CandleParams::getPairs)
            .orElseGet(() -> new NameValuePair[0]);

        final var request = getRequestBuilder(getURI(
            String.format("%s/candles", market),
            Stream.concat(
                    Stream.of(createParameter("interval", interval.serialize())),
                    Arrays.stream(params))
                .toArray(NameValuePair[]::new)))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(request, new TypeReference<>() {})));
    }

    @Override
    public Single<List<Candle24h>> getCandle24h() {
        final var request = getRequestBuilder(getURI("ticker/24h"))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(request, new TypeReference<>() {})));
    }

    @Override
    public Single<Candle24h> getCandle24h(final String market) {
        final var request = getRequestBuilder(getURI("ticker/24h", createParameter("market", market)))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(request, Candle24h.class)));
    }

    @Override
    public Single<TransactionHistory> getTransactionHistory() {
        return getTransactionHistory(null);
    }

    @Override
    public Single<TransactionHistory> getTransactionHistory(final TransactionHistoryParams transactionHistoryParams) {
        final var params = Optional.ofNullable(transactionHistoryParams).map(TransactionHistoryParams::getPairs)
            .orElseGet(() -> new NameValuePair[0]);

        final var request = getRequestBuilder(getURI("account/history", params))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(withAuthentication(request), TransactionHistory.class)));
    }

    public Single<MarketFee> getMarketFee() {
        return getMarketFee(null);
    }

    @Override
    public Single<MarketFee> getMarketFee(final MarketFeeParams marketFeeParams) {
        final var params = Optional.ofNullable(marketFeeParams).map(MarketFeeParams::getPairs)
            .orElseGet(() -> new NameValuePair[0]);

        final var request = getRequestBuilder(getURI("account/fees", params))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(withAuthentication(request), MarketFee.class)));
    }

    @Override
    public Single<List<Balance>> getBalance() {
        final var request = getRequestBuilder(getURI("balance"))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(withAuthentication(request), new TypeReference<>() {})));
    }

    @Override
    public Single<List<Balance>> getBalance(final String symbol) {
        final var request = getRequestBuilder(getURI("balance", createParameter("symbol", symbol)))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(withAuthentication(request), new TypeReference<>() {})));
    }

    @Override
    public Single<AccountFee> getAccountFee() {
        final var request = getRequestBuilder(getURI("account"))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(withAuthentication(request), AccountFee.class)));
    }

    @Override
    public Single<List<WithdrawalHistory>> getWithdrawalHistory() {
        final var request = getRequestBuilder(getURI("withdrawalHistory"))
            .GET()
            .build();

        return withIOScheduler(Single.fromFuture(sendAsync(withAuthentication(request), new TypeReference<>() {})));
    }

    private HttpRequest.Builder getRequestBuilder(final URI uri) {
        return HttpRequest.newBuilder()
            .uri(uri)
            .timeout(Duration.ofSeconds(10));
    }

    private HttpRequest withAuthentication(final HttpRequest request) {
        return withAuthentication(request, Optional.empty());
    }

    private <T> HttpRequest withAuthentication(
        final HttpRequest request,
        final Optional<T> payload
    ) {
        final var timestamp = Instant.now().toEpochMilli();
        final var creds = credentials.orElseThrow(() -> new IllegalStateException("Credentials should be present"));

        try {
            final Optional<byte[]> body;
            if (payload.isPresent()) {
                body = Optional.of(objectMapper.writeValueAsBytes(payload));
            } else {
                body = Optional.empty();
            }

            final var relativePath = request.uri().toString().replaceAll(BASE_URL, "");
            final var signature = CryptoUtils.createSignature(
                request.method(),
                relativePath,
                body,
                timestamp,
                creds.apiSecret()
            );

            return HttpRequest.newBuilder(request, (s1, s2) -> true)
                .header(HEADER_ACCESS_KEY, creds.apiKey())
                .header(HEADER_ACCESS_SIGNATURE, signature)
                .header(HEADER_ACCESS_TIMESTAMP, String.valueOf(timestamp))
                .header(HEADER_ACCESS_WINDOW, String.valueOf(windowAccesTime))
                .timeout(Duration.ofMillis(windowAccesTime))
                .build();
        } catch (final JsonProcessingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
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
        return withRateLimit(httpClient.sendAsync(request, new JsonBodyHandler<T>(type, objectMapper)));
    }

    private void updateRateLimit(final HttpHeaders headers) {
        final var limit = headers.firstValue(HEADER_RATE_LIMIT_LIMIT).map(Integer::parseInt);
        final var remaining = headers.firstValue(HEADER_RATE_LIMIT_REMAINING).map(Integer::parseInt);
        final var resetAt = headers.firstValue(HEADER_RATE_LIMIT_RESET_AT).map(Long::parseLong);

        limit.flatMap(lim -> remaining.flatMap(rem -> resetAt.map(res -> RateLimitQuota.of(lim, rem, res))))
            .ifPresent(rateLimitQuota::onNext);
    }
}
