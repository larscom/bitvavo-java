package io.github.larscom.bitvavo.http.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.util.Set;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableAccountFee.class, using = AccountFeeDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface AccountFee {
    /// The price tier used to calculate your trading fees.
    ///
    /// The first tier in the Bitvavo fee structure is "0".
    ///
    /// See: [Fees](https://bitvavo.com/en/fees)
    String getTier();

    /// The fee for trades that take liquidity from the order book.
    BigDecimal getTaker();

    /// The fee for trades that add liquidity to the order book.
    BigDecimal getMaker();

    /// Your trading volume in the last 30 days measured in EUR.
    BigDecimal getVolume();

    Set<Capability> getCapabilities();
}
