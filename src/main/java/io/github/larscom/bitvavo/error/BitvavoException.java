package io.github.larscom.bitvavo.error;

import io.reactivex.rxjava3.annotations.NonNull;

public class BitvavoException extends RuntimeException {
    private final BitvavoError error;

    public BitvavoException(@NonNull final BitvavoError error) {
        super(messageFromError(error));
        this.error = error;
    }

    public BitvavoError getError() {
        return error;
    }

    private static String messageFromError(@NonNull final BitvavoError error) {
        return String.format("[%d]: %s", error.getErrorCode(), error.getErrorMessage());
    }
}