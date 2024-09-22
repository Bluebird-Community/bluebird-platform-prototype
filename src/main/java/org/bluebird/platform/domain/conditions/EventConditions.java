package org.bluebird.platform.domain.conditions;

import org.bluebird.platform.domain.model.EventDTO;

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

    public static EventCondition matchesUei(final String uei) {
        return new EventCondition() {
            @Override
            public String getDescription() {
                return "uei == '%s'".formatted(uei);
            }

            @Override
            public boolean matches(EventDTO event) {
                return event.getUei().equals(uei);
            }
        };
    }

    // TODO MVR ...
//    public static EventCondition matchesLevel(int level) {
//        return new EventCondition() {
//
//            @Override
//            public String getDescription() {
//                return "level == %s".formatted(level);
//            }
//
//            @Override
//            public boolean matches(EventDTO event) {
//                return event.getLevel() != null && event.getLevel() == level;
//            }
//        };
//    }

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

    // TODO MVR ...
//    public static Condition<EventDTO> nullOrZeroLevel() {
//        return Conditions.or(
//                EventConditions.matchesNullValue("level", EventDTO::getLevel),
//                EventConditions.matchesLevel(0)
//        );
//    }
}
