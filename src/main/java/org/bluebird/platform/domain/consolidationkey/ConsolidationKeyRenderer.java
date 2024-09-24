package org.bluebird.platform.domain.consolidationkey;

import lombok.AllArgsConstructor;
import org.bluebird.platform.domain.model.EventDTO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.beans.BeanDescriptor;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
@AllArgsConstructor
public class ConsolidationKeyRenderer {

    private final ConsolidationTokenParser tokenParser;

    public String render(String input, EventDTO event) {
        return render(input, event.asMap());
    }

    public String render(String input, Map<String, Object> properties) {
        final var tokens = tokenParser.parse(input);
        return tokens.stream().map(opennmsToken -> substitute(properties, opennmsToken))
                .map(it -> it == null ? "" : it)
                .collect(Collectors.joining(""));

    }

    private String substitute(Map<String, Object> properties, ConsolidationToken token) {
        // TODO MVR ensure this is not needed, as it is ugly
        return switch (token.type()) {
            case EMPTY -> "";
            case NULL -> "null";
            case CONSTANT -> token.token();
            case PARAMETER -> {
                // TODO MVR this is almost identical to EventConditions :-/
                final var eventParameterPath = token.token();
                if (!eventParameterPath.contains(".")) {
                    final var value = properties.get(eventParameterPath);
                    yield value == null ? "null" : value.toString();
                }
                final var path = eventParameterPath.split("\\.");
                Object currentProperties = properties;
                for (int i = 0; i < path.length; i++) {
                    if (i < path.length - 1) {
                        if (currentProperties instanceof Map<?, ?>) {
                            currentProperties = ((Map<?, ?>) currentProperties).get(path[i]);
                        } else if (currentProperties instanceof Object) { // Try bean access
                            currentProperties = new BeanDescriptor(currentProperties.getClass()).getValue(path[i]);
                        }
                    } else {
                        // Reached the end of the line
                        if (currentProperties instanceof Map<?, ?>) {
                            final var value = ((Map<?, ?>) currentProperties).get(path[i]);
                            yield value == null ? "null" : value.toString();
                        } else if (currentProperties instanceof Object) {  // Try bean access
                            final var value = new BeanDescriptor(currentProperties.getClass()).getValue(path[i]);
                            yield value == null ? "null" : value.toString();
                        }
                    }
                }
                throw new IllegalStateException("Cannot substitute provided token: '%s'".formatted(token.token()));
            }
        };
    }

}
