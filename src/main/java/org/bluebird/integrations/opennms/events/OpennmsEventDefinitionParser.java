package org.bluebird.integrations.opennms.events;

import org.bluebird.integrations.opennms.events.substitution.OpennmsToken;
import org.bluebird.integrations.opennms.events.substitution.OpennmsTokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

// TODO MVR rename this: it's responsibility is to create tokens from a reduction key,e.g. %uei%
public class OpennmsEventDefinitionParser {

    public List<OpennmsToken> parse(String input) {
        final var matcher = Pattern.compile("%([a-zA-Z-_]+|parm\\[[a-zA-Z-_.]+\\])%").matcher(input);
        final var tokens = new ArrayList<OpennmsToken>();
        var startIndex = 0;
        while (matcher.find()) {
            if (startIndex != matcher.start()) {
                // This is a constant token
                final var token = input.substring(startIndex, matcher.start());
                tokens.add(new OpennmsToken(token, OpennmsTokenType.CONSTANT));
            }
            final var token = input.substring(matcher.start(), matcher.end());
            final var tokenType = determineTokenType(token);
            tokens.add(new OpennmsToken(clean(token), tokenType));
            startIndex = matcher.end();
        }
        // In this case, the end of the string is missing
        if (startIndex != input.length()) {
            tokens.add(new OpennmsToken(input.substring(startIndex), OpennmsTokenType.CONSTANT));
        }
        if (tokens.isEmpty()) {
            return List.of(new OpennmsToken(input, OpennmsTokenType.CONSTANT));
        }
        return tokens;
    }

    private String clean(String token) {
        return token.substring(1, token.length() - 1);
    }

    private OpennmsTokenType determineTokenType(String token) {
        if (token.startsWith("%") && token.endsWith("%")) {
            final var newToken = clean(token);
            return Stream.of(OpennmsTokenType.values())
                    .filter(it -> it.matches(newToken))
                    .findFirst()
                    .orElseThrow();
        }
        return OpennmsTokenType.CONSTANT;
    }

}