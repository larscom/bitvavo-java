package io.github.larscom.bitvavo.http.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Capability {
    BUY("buy"),
    SELL("sell"),
    DEPOSIT_CRYPTO("depositCrypto"),
    DEPOSIT_FIAT("depositFiat"),
    WITHDRAW_CRYPTO("withdrawCrypto"),
    WITHDRAW_FIAT("withdrawFiat");

    private final String value;

    Capability(final String value) {
        this.value = value;
    }

    @JsonValue
    public String serialize() {
        return value;
    }

    @JsonCreator
    public static Capability deserialize(final String value) {
        return Arrays.stream(values())
            .filter(c -> c.value.equals(value))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid capability: " + value));
    }
}