package io.github.larscom.websocket;

public final class Either<L, R> {
    private final L left;
    private final R right;

    private Either(final L left, final R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> Either<L, R> left(final L value) {
        return new Either<>(value, null);
    }

    public static <L, R> Either<L, R> right(final R value) {
        return new Either<>(null, value);
    }

    public boolean isLeft() {
        return left != null;
    }

    public boolean isRight() {
        return right != null;
    }

    public L getLeft() {
        assert isLeft();
        return left;
    }

    public R getRight() {
        assert isRight();
        return right;
    }
}