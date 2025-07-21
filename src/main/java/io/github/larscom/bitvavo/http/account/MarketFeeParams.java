package io.github.larscom.bitvavo.http.account;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.immutables.value.Value;

import java.util.ArrayList;
import java.util.Optional;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
public interface MarketFeeParams {
    /// The market for which to get fee information.
    /// If not specified, returns the fees for your current tier in [Category B](https://bitvavo.com/en/fees)
    Optional<String> getMarket();

    /// The quote currency for which to get fee information.
    /// If not specified, returns the fees for your current tier in [Category B](https://bitvavo.com/en/fees)
    Optional<Quote> getQuote();

    default NameValuePair[] getPairs() {
        final var pairs = new ArrayList<NameValuePair>();

        getMarket().ifPresent(market ->
            pairs.add(new BasicNameValuePair("market", market))
        );

        getQuote().ifPresent(quote ->
            pairs.add(new BasicNameValuePair("quote", quote.name()))
        );

        return pairs.toArray(NameValuePair[]::new);
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableMarketFeeParams.Builder {
    }
}
