package io.github.larscom.internal;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class Crypto {

    public static String createSignature(
        final String httpMethod,
        final String relativePath,
        final Optional<byte[]> body,
        final long timestamp,
        final String apiSecret
    ) throws InvalidKeyException, NoSuchAlgorithmException {
        final var parts = new StringBuilder()
            .append(timestamp)
            .append(httpMethod)
            .append("/v2")
            .append(relativePath);

        body.ifPresent(b -> parts.append(new String(b, UTF_8)));

        final var instance = "HmacSHA256";
        final var mac = Mac.getInstance(instance);
        final var secretKey = new SecretKeySpec(apiSecret.getBytes(UTF_8), instance);
        mac.init(secretKey);

        final byte[] hmacBytes = mac.doFinal(parts.toString().getBytes(UTF_8));

        return HexFormat.of().formatHex(hmacBytes);
    }
}
