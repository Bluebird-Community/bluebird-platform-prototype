package org.bluebird.platform.domain.consolidationkey;

import lombok.AllArgsConstructor;
import org.bluebird.platform.domain.model.EventDTO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
@AllArgsConstructor
public class ConsolidationKeyRenderer {

    private final ConsolidationTokenParser tokenParser;

    public String render(String input, EventDTO event) {
        final var properties = event.asMap();
        return render(input, properties);
    }

    public String render(String input, Map<String, String> properties) {
        final var tokens = tokenParser.parse(input);
        return tokens.stream().map(opennmsToken -> substitute(properties, opennmsToken))
                .map(it -> it == null ? "" : it)
                .collect(Collectors.joining(""));

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
