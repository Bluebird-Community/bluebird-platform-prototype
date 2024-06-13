package org.bluebird.platform.api.events.definitions.parser;

import org.bluebird.platform.api.events.definitions.Token;
import org.bluebird.platform.api.events.definitions.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class EventDefinitionParser {

    public List<Token> parse(String input) {

        final var matcher = Pattern.compile("%([a-zA-Z-_]+|parm\\[[a-zA-Z-_.]+\\])%").matcher(input);
        final var tokens = new ArrayList<Token>();
        var startIndex = 0;
        while (matcher.find()) {
            if (startIndex != matcher.start()) {
                // This is a constant token
                final var token = input.substring(startIndex, matcher.start());
                tokens.add(new Token(token, TokenType.CONSTANT));
            }
            final var token = input.substring(matcher.start(), matcher.end());
            final var tokenType = determineTokenType(token);
            tokens.add(new Token(clean(token), tokenType));
            startIndex = matcher.end();
        }
        // In this case, the end of the string is missing
        if (startIndex != input.length()) {
            tokens.add(new Token(input.substring(startIndex), TokenType.CONSTANT));
        }
        if (tokens.isEmpty()) {
            return List.of(new Token(input, TokenType.CONSTANT));
        }
        return tokens;
    }

    private String clean(String token) {
        return token.substring(1, token.length() - 1);
    }

    private TokenType determineTokenType(String token) {
        if (token.startsWith("%") && token.endsWith("%")) {
            final var newToken = clean(token);
            return Stream.of(TokenType.values())
                    .filter(it -> it.matches(newToken))
                    .findFirst()
                    .orElseThrow();
        }
        return TokenType.CONSTANT;
    }

}