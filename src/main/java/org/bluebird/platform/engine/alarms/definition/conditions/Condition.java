package org.bluebird.platform.engine.alarms.definition.conditions;

/**
 * Condition object to help configuring alarm defintions within bluebird.
 * This is similar to a matcher, e.g. from Hamcrest, but at the moment we call them Condition.
 *
 * @param <T>
 */
public interface Condition<T> {
    default String describe(Object context) {
        return "TODO MVR"; // TODO MVR ...
    }
    String getDescription();
    boolean matches(T event);
}
