package org.bluebird.platform.domain.alarms;

import lombok.Getter;
import org.bluebird.platform.domain.model.AlarmSeverity;
import org.bluebird.platform.domain.conditions.Condition;
import org.bluebird.platform.domain.conditions.Conditions;
import org.bluebird.platform.domain.model.EventDTO;

import java.util.Objects;

@Getter
public class AlarmDefinition {
    private final String namespace;
    private final Condition<EventDTO> raiseCondition;
    private final Condition<EventDTO> clearCondition;
    private final String label;
    private final String description;
    private final AlarmSeverity severity;
    private final Integer level;

    private AlarmDefinition(Builder builder) {
        Objects.requireNonNull(builder);
        // TODO MVR add validation ...
        this.raiseCondition = builder.raiseCondition;
        this.clearCondition = builder.clearCondition;
        this.namespace = builder.namespace;
        this.label = builder.label;
        this.description = builder.description;
        this.severity = builder.severity;
        this.level = builder.level == null || builder.level <= 0 ? 1 : builder.level;
    }

    public String getDescription() {
        return """
                Namespace: %s
                Label: %s
                Description: %s
                Severity: %s
                Level: %s
                Raise:
                    on: %s
                Clear:
                    on: %s
                """
                .formatted(
                        namespace,
                        label,
                        description,
                        severity,
                        level,
                        ident(raiseCondition.getDescription()),
                        ident(clearCondition.getDescription()))
                ;
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
        private Condition<EventDTO> raiseCondition = Conditions.none();
        private Condition<EventDTO> clearCondition = Conditions.none();
        private AlarmSeverity severity;
        private String label;
        private String namespace;
        private Integer level;
        private String description;

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

        public Builder withNamespace(String namespace) {
            this.namespace = namespace;
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

        public Builder withRaiseCondition(Condition<EventDTO> condition) {
            this.raiseCondition = Objects.requireNonNull(condition);
            return this;
        }

        public Builder withClearCondition(Condition<EventDTO> condition) {
            this.clearCondition = Objects.requireNonNull(condition);
            return this;
        }

        public AlarmDefinition build() {
            return new AlarmDefinition(this);
        }
    }

}
