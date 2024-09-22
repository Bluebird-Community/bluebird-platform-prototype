package org.bluebird.platform.domain.consolidationkey;

import org.bluebird.platform.domain.model.EventDTO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Scope("prototype")
public class ConsolidationKeyRenderer {

    public String render(String input, EventDTO event) {
        final var properties = event.asMap();
        return render(input, properties);
    }

    public String render(String input, Map<String, String> properties) {
        final var tokens = determineTokens(input);
        return tokens.stream().map(opennmsToken -> substitute(properties, opennmsToken))
                .map(it -> it == null ? "" : it)
                .collect(Collectors.joining(""));

    }

    private List<ConsolidationToken> determineTokens(String input) {
        final var matcher = Pattern.compile("\\{(.+)\\}").matcher(input);
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

    private String substitute(Map<String, String> properties, ConsolidationToken token) {
        // TODO MVR ensure this is not needed, as it is ugly
        return switch (token.type()) {
            case EMPTY -> "";
            case NULL -> "null";
            case CONSTANT -> token.token();
            case PARAMETER -> properties.getOrDefault(token.token(), "null");
        };
    }

}
