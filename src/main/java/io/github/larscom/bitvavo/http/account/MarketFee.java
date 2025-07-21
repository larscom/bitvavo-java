package io.github.larscom.bitvavo.http.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableMarketFee.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface MarketFee {
    /// The price tier used to calculate your trading fees.
    ///
    /// The first tier in the Bitvavo fee structure is "0".
    ///
    /// See: [Fees](https://bitvavo.com/en/fees)
    String getTier();

    /// Your trading volume over the last 30 days in EUR.
    BigDecimal getVolume();

    /// The fee for trades that take liquidity from the order book.
    BigDecimal getTaker();

    /// The fee for trades that add liquidity to the order book.
    BigDecimal getMaker();
}
