package org.bluebird.platform.util;

public record Triple<L, M, R>(L left, M middle, R right) {

    public static <L, M, R> Triple<L, M, R> of(L left, M middle, R right) {
        return new Triple<>(left, middle, right);
    }

    public L first() {
        return left;
    }

    public M second() {
        return middle;
    }

    public R third() {
        return right;
    }

}



