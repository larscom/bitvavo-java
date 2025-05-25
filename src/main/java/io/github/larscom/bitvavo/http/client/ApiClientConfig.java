package io.github.larscom.bitvavo.http.client;

import org.immutables.value.Value;

import java.net.InetSocketAddress;
import java.util.Optional;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
public interface ApiClientConfig {
    /// Credentials in the form of an apiKey and apiSecret.
    Optional<Credentials> getCredentials();

    /// InetSocketAddress for the HTTP proxy.
    Optional<InetSocketAddress> getProxyAddress();

    /// The execution timeout in milliseconds after Bitvavo-Access-Timestamp header value.
    /// The default value is 10000, the maximum value is 60000.
    Optional<Integer> getAccessWindowTime();

    @Value.Check
    default void check() {
        getAccessWindowTime().ifPresent(time -> {
            if (time <= 0 || time > 600000) {
                throw new IllegalStateException("Cannot build ApiClientConfig, access window time must be > 0 and <= 60000");
            }
        });
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableApiClientConfig.Builder {
    }
}
