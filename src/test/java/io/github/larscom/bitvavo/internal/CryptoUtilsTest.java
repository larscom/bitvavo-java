package io.github.larscom.bitvavo.internal;

import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CryptoUtilsTest {

    @Test
    void testCreateSignatureWithBody() throws NoSuchAlgorithmException, InvalidKeyException {
        final var body = "{\"market\":\"ETH-EUR\",\"amount\":1.5,\"price\":2500.5}";
        final var timestamp = 1721452468484L;

        final var signature = CryptoUtils.createSignature("GET", "/test", Optional.of(body.getBytes()), timestamp, "API_SECRET");

        assertThat(signature).isEqualTo("cf9f81048eccf714305dfd0147252a38de6788ec343f4466a124ffe7c524ded8");
    }

    @Test
    void testCreateSignatureNoBody() throws NoSuchAlgorithmException, InvalidKeyException {
        final var timestamp = 1721452468484L;
        final var signature = CryptoUtils.createSignature("GET", "/test", Optional.empty(), timestamp, "API_SECRET");

        assertThat(signature).isEqualTo("dce7f0d49d559d6012733af234fa2bdef5a8492842726405e5c0b514f9bf1f55");
    }

}