package org.bluebird.platform.domain.consolidationkey;

import java.util.Objects;
import java.util.function.Predicate;

public enum ConsolidationTokenType {

    PARAMETER(token -> token.startsWith("{") && token.endsWith("}")),
    EMPTY(token -> token != null && token.isEmpty()),
    NULL(Objects::isNull),
    CONSTANT((token) -> true),
    ;

    private final Predicate<String> tokenMatcher;

    ConsolidationTokenType(Predicate<String> tokenMatcher) {
        this.tokenMatcher = Objects.requireNonNull(tokenMatcher);
    }

    public boolean matches(String token) {
        return tokenMatcher.test(token);
    }
}
