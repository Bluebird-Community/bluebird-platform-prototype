package org.bluebird.platform.domain.consolidationkey;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
@Scope("prototype")
public class ConsolidationTokenParser {

    public List<ConsolidationToken> parse(String input) {
        final var matcher = Pattern.compile("\\{([a-zA-Z-_0-9]+(\\.[a-zA-Z-_0-9]+)*)\\}").matcher(input);
        final var tokens = new ArrayList<ConsolidationToken>();
        var startIndex = 0;
        while (matcher.find()) {
            if (startIndex != matcher.start()) {
                // This is a constant token
                final var token = input.substring(startIndex, matcher.start());
                tokens.add(new ConsolidationToken(token, ConsolidationTokenType.CONSTANT));
            }
            final var token = input.substring(matcher.start(), matcher.end());
            final var tokenType = determineTokenType(token);
            tokens.add(new ConsolidationToken(cleanToken(token), tokenType));
            startIndex = matcher.end();
        }
        // In this case, the end of the string is missing
        if (startIndex != input.length()) {
            tokens.add(new ConsolidationToken(input.substring(startIndex), ConsolidationTokenType.CONSTANT));
        }
        if (tokens.isEmpty()) {
            return List.of(new ConsolidationToken(input, ConsolidationTokenType.CONSTANT));
        }
        return tokens;
    }

    private String cleanToken(String token) {
        return token.substring(1, token.length() - 1);
    }

    private ConsolidationTokenType determineTokenType(String token) {
        return Stream.of(ConsolidationTokenType.values())
                .filter(it -> it.matches(token))
                .findFirst()
                .orElseThrow();
    }
}
