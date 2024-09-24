package org.bluebird.platform.domain.conditions;

import org.bluebird.platform.domain.model.EventDTO;

import java.beans.BeanDescriptor;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public final class EventConditions extends Conditions {

    private EventConditions() {

    }

    public static EventCondition and(EventCondition... conditions) {
        final var and = Conditions.and(conditions);
        return new EventCondition() {

            @Override
            public String getDescription() {
                return "Event: %s".formatted(and.getDescription());
            }

            @Override
            public boolean matches(EventDTO event) {
                return and.matches(event);
            }
        };
    }

    public static EventCondition or(EventCondition... conditions) {
        final var or = Conditions.or(conditions);
        return new EventCondition() {

            @Override
            public String getDescription() {
                return "Event: %s".formatted(or.getDescription());
            }

            @Override
            public boolean matches(EventDTO event) {
                return or.matches(event);
            }
        };
    }

    public static EventCondition matchesNamespace(final String namespace) {
        return new EventCondition() {
            @Override
            public String getDescription() {
                return "namespace == '%s'".formatted(namespace);
            }

            @Override
            public boolean matches(EventDTO event) {
                return event.getNamespace().equals(namespace);
            }
        };
    }

    public static EventCondition matchesNullValue(String fieldName, Function<EventDTO, Object> valueExtractor) {
        Objects.requireNonNull(fieldName);
        Objects.requireNonNull(valueExtractor);
        return new EventCondition() {

            @Override
            public String getDescription() {
                return "%s is null".formatted(fieldName);
            }

            @Override
            public boolean matches(EventDTO event) {
                final var value = valueExtractor.apply(event);
                return value == null;
            }
        };
    }

    public static EventCondition matches(String eventParameterPath, String expectedValue) {
        Objects.requireNonNull(eventParameterPath);
        Objects.requireNonNull(expectedValue);
        return new EventCondition() {

            @Override
            public String getDescription() {
                return "%s matches %s".formatted(eventParameterPath, expectedValue);
            }

            // TODO MVR refactory this, as it is ugly as hell, but should work for now
            @Override
            public boolean matches(EventDTO event) {
                if (!eventParameterPath.contains(".")) {
                    return event.asMap().get(eventParameterPath).equals(expectedValue);
                }
                final var path = eventParameterPath.split("\\.");
                Object payload = event.getPayload();
                for (int i = 0; i < path.length; i++) {
                    if (i < path.length - 1) {
                        if (payload instanceof Map<?, ?>) {
                            payload = ((Map<?, ?>) payload).get(path[i]);
                        } else if (payload instanceof Object) { // Try bean access
                            payload = new BeanDescriptor(payload.getClass()).getValue(path[i]);
                        }
                    } else {
                        if (payload instanceof Map<?, ?>) {
                            return Objects.equals(((Map<?, ?>) payload).get(path[i]), expectedValue);
                        } else if (payload instanceof Object) {  // Try bean access
                            return Objects.equals(new BeanDescriptor(payload.getClass()).getValue(path[i]), expectedValue);
                        }
                    }
                }
                return false;
            }
        };
    }

    public static EventCondition not(EventCondition condition) {
        return new EventCondition() {

            @Override
            public String getDescription() {
                return "not (%s)".formatted(condition.getDescription());
            }

            @Override
            public boolean matches(EventDTO event) {
                return !condition.matches(event);
            }
        };
    }
}
