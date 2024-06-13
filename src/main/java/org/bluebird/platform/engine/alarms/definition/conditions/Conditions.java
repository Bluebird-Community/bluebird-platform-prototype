package org.bluebird.platform.engine.alarms.definition.conditions;

import org.bluebird.platform.engine.events.EventDTO;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Conditions {

    Conditions() {
    }

    public static <X, T extends Condition<X>> Condition<X> wrap(T condition, Function<String, String> descriptionDecorator) {
        Objects.requireNonNull(condition);
        Objects.requireNonNull(descriptionDecorator);
        return new Condition<>() {

            @Override
            public String getDescription() {
                return descriptionDecorator.apply(condition.getDescription());
            }

            @Override
            public boolean matches(X event) {
                return condition.matches(event);
            }
        };

    }

    public static <X, T extends Condition<X>> Condition<X> matchesAll(T... conditions) {
        return and(conditions);
    }

    public static <X, T extends Condition<X>> Condition<X> matchesAny(T... conditions) {
        return or(conditions);
    }

    public static <X, T extends Condition<X>> Condition<X> and(T... conditions) {
        return new Condition<>() {

            @Override
            public String getDescription() {
                return Stream.of(conditions)
                        .map(T::getDescription)
                        .collect(Collectors.joining("\n\tAND\n"));
            }

            @Override
            public boolean matches(X object) {
                return Stream.of(conditions).allMatch(condition -> condition.matches(object));
            }
        };
    }

    public static <X, T extends Condition<X>> Condition<X> or(T... conditions) {
        return new Condition<>() {

            @Override
            public String getDescription() {
                return Stream.of(conditions)
                        .map(T::getDescription)
                        .collect(Collectors.joining("\n\tOR\n"));
            }

            @Override
            public boolean matches(X object) {
                return Stream.of(conditions).anyMatch(condition -> condition.matches(object));
            }
        };
    }

    public static <X, T extends Condition<X>> Condition<X> none() {
        return new Condition<>() {
            @Override
            public String getDescription() {
                return "NO_MATCH";
            }

            @Override
            public boolean matches(X object) {
                return false;
            }
        };
    }

}
