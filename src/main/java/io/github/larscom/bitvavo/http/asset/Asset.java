package io.github.larscom.bitvavo.http.asset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableAsset.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Asset {
    /// The asset for which the information is returned.
    String getSymbol();

    /// The full name of the asset.
    String getName();

    /// The number of decimal digits for this asset.
    Integer getDecimals();

    /// The fee for depositing the asset.
    BigDecimal getDepositFee();

    /// The minimum number of network confirmations to credit the asset to your account.
    Integer getDepositConfirmations();

    /// The status of the asset being deposited.
    DepositStatus getDepositStatus();

    /// The fee for withdrawing the asset.
    BigDecimal getWithdrawalFee();

    /// The minimum amount that can be withdrawn.
    BigDecimal getWithdrawalMinAmount();

    /// The status of the asset being withdrawn.
    WithdrawalStatus getWithdrawalStatus();

    /// The list of supported networks.
    Set<String> getNetworks();

    /// The reason if the withdrawalStatus or depositStatus is not OK.
    @JsonDeserialize(using = EmptyStringToOptionalDeserializer.class)
    Optional<String> getMessage();
}
