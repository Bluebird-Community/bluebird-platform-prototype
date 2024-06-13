package org.bluebird.platform.util;

public record Tuple<L, R>(L left, R right) {
    public static <L, R> Tuple<L, R> of(L left, R right) {
        return new Tuple<>(left, right);
    }

    public L first() {
        return left;
    }

    public R second() {
        return right;
    }

}

