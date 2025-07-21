package io.github.larscom.bitvavo.http.client;

import java.time.Instant;

public final class RateLimitQuota {
    private final int limit;
    private final int remaining;
    private final Instant resetAt;

    public static RateLimitQuota DEFAULT = RateLimitQuota.of(1000, 1000, Instant.now().toEpochMilli());

    private RateLimitQuota(final int limit, final int remaining, final long resetAt) {
        this.limit = limit;
        this.remaining = remaining;
        this.resetAt = Instant.ofEpochMilli(resetAt);
    }

    public static RateLimitQuota of(final int limit, final int remaining, final long resetAt) {
        return new RateLimitQuota(limit, remaining, resetAt);
    }

    public boolean isExhausted() {
        return remaining < 5 && resetAt.isAfter(Instant.now());
    }

    public Instant getResetAt() {
        return resetAt;
    }

    public int getRemaining() {
        return remaining;
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public String toString() {
        return "RateLimitQuota{" +
            "limit=" + limit +
            ", remaining=" + remaining +
            ", resetAt=" + resetAt +
            '}';
    }
}
