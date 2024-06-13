package org.bluebird.platform.engine.alarms.definition;

import lombok.Getter;
import org.bluebird.platform.engine.alarms.AlarmSeverity;
import org.bluebird.platform.engine.alarms.definition.conditions.Condition;
import org.bluebird.platform.engine.alarms.definition.conditions.Conditions;
import org.bluebird.platform.engine.events.EventDTO;

import java.util.Objects;

@Getter
public class AlarmDefinition {
    private final String uei;
    private final Condition<EventDTO<?>> raiseCondition;
    private final Condition<EventDTO<?>> clearCondition;
    private final String raiseKey;
    private final String clearKey;
    private final String label;
    private final String description;
    private final AlarmSeverity severity;
    private final Integer level;

    private AlarmDefinition(Builder builder) {
        Objects.requireNonNull(builder);
        // TODO MVR add validation ...
        this.raiseCondition = builder.raiseCondition;
        this.clearCondition = builder.clearCondition;
        this.raiseKey = builder.raiseKey;
        this.clearKey = builder.clearKey;
        this.uei = builder.uei;
        this.label = builder.label;
        this.description = builder.description;
        this.severity = builder.severity;
        this.level = builder.level == null || builder.level <= 0 ? 1 : builder.level;
        if (raiseKey != null && clearKey != null && !Objects.equals(raiseKey, clearKey)) {
            throw new IllegalStateException("This alarm can never be resolved by the provided alarm definition");
        }
    }

    public String getDescription() {
        return """
                UEI: %s
                Label: %s
                Description: %s
                Severity: %s
                Level: %s
                Raise:
                    on: %s
                    key: %s
                Clear:
                    on: %s
                    key: %s
                """
                .formatted(
                        uei,
                        label,
                        description,
                        severity,
                        level,
                        ident(raiseCondition.getDescription()),
                        ident(raiseKey),
                        ident(clearCondition.getDescription()),
                        ident(clearKey));
    }

    private static String ident(String value) {
        if (value == null) {
            return "none";
        }
        return value.replaceAll("\n", "\n\t\t");
    }

    public static AlarmDefinition.Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private Condition<EventDTO<?>> raiseCondition = Conditions.none();
        private Condition<EventDTO<?>> clearCondition = Conditions.none();
        private AlarmSeverity severity;
        private String label;
        private String uei;
        private Integer level;
        private String description;
        private String raiseKey;
        private String clearKey;

        private Builder() {

        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withLabel(String label) {
            this.label = label;
            return this;
        }

        public Builder withUei(String uei) {
            this.uei = uei;
            return this;
        }

        public Builder withSeverity(AlarmSeverity severity) {
            this.severity = severity;
            return this;
        }

        public Builder withLevel(Integer level) {
            this.level = level;
            return this;
        }

        public Builder withRaise(Condition<EventDTO<?>> condition, String raiseKey) {
            this.raiseCondition = Objects.requireNonNull(condition);
            this.raiseKey = raiseKey;
            return this;
        }

        public Builder withClear(Condition<EventDTO<?>> condition, String clearKey) {
            this.clearCondition = Objects.requireNonNull(condition);
            this.clearKey = clearKey;
            return this;
        }

        public Builder withRaiseCondition(Condition<EventDTO<?>> condition) {
            this.raiseCondition = Objects.requireNonNull(condition);
            return this;
        }

        public Builder withClearCondition(Condition<EventDTO<?>> condition) {
            this.clearCondition = Objects.requireNonNull(condition);
            return this;
        }

        public AlarmDefinition build() {
            return new AlarmDefinition(this);
        }
    }

}
