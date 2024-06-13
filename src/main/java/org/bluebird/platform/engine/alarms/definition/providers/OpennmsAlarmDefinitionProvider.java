package org.bluebird.platform.engine.alarms.definition.providers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bluebird.platform.api.events.definitions.OpennmsEventDefinitionProvider;
import org.bluebird.platform.engine.alarms.AlarmSeverity;
import org.bluebird.platform.engine.alarms.definition.AlarmDefinition;
import org.bluebird.platform.engine.alarms.definition.ConsolidationKeyProviders;
import org.bluebird.platform.engine.alarms.definition.conditions.ClearConditions;
import org.bluebird.platform.engine.alarms.definition.conditions.RaiseConditions;
import org.opennms.integration.xml.eventconf.events.xml.XmlEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

// Integration with OpenNMS
@AllArgsConstructor
@Component
@Slf4j
public class OpennmsAlarmDefinitionProvider implements AlarmDefinitionProvider {

    private final OpennmsEventDefinitionProvider eventDefinitionProvider;

    @Override
    public List<AlarmDefinition> getAlarmDefinitions() {
        final var alarmRelatedOnmsEvents = eventDefinitionProvider
                .getEventDefinitions().stream()
                .filter(onmsEventDefinition -> onmsEventDefinition.getAlarmData() != null)
                .toList();

        // Find all raising events
        return alarmRelatedOnmsEvents.stream()
                .filter(onmsEventDefinition ->
                        onmsEventDefinition.getAlarmData().getReductionKey() != null
                                && onmsEventDefinition.getAlarmData().getAlarmType() != 2
                )
                .map(onmsEventDefinition -> {
                    // We do this, to allow for resolution, as the clear key contains the hard-coded uei of the raising event
                    // However, the original reductionKey may have used %uei% instead, so this makes it possible to actually
                    // find a resolving event
                    final var reductionKeyTemplate = onmsEventDefinition.getAlarmData().getReductionKey();
                    final var reductionKey = reductionKeyTemplate.replaceAll("%uei%", onmsEventDefinition.getUei());

                    // Is there a clearing event available?
                    final var clearingEventDefinition = findClearingEventForReductionKeyTemplate(alarmRelatedOnmsEvents, reductionKey);
                    return AlarmDefinition.newBuilder()
                            .withUei(onmsEventDefinition.getUei())
                            .withLabel(onmsEventDefinition.getEventLabel().contains("event") ? "Alarm from '%s' event".formatted(onmsEventDefinition.getUei()) : onmsEventDefinition.getEventLabel())
                            .withDescription("TBD")
                            .withSeverity(parseSeverity(onmsEventDefinition.getSeverity()))
                            .withRaise(
                                    RaiseConditions.matchesEvent(onmsEventDefinition),
                                    ConsolidationKeyProviders.template(reductionKey)
                            )
                            .withClear(
                                    ClearConditions.matchesEvent(clearingEventDefinition),
                                    ConsolidationKeyProviders.template(clearingEventDefinition)
                            )
                            .build();
                }).toList();
    }

    private Optional<XmlEvent> findClearingEventForReductionKeyTemplate(List<XmlEvent> alarmEvents, String reductionKey) {
        return alarmEvents.stream()
                .filter(it -> Objects.equals(it.getAlarmData().getClearKey(), reductionKey))
                .findFirst();
    }

    static AlarmSeverity parseSeverity(String severity) {
        if (severity.equalsIgnoreCase("INDETERMINATE")) {
            return AlarmSeverity.INDETERMINATE;
        }
        if (severity.equalsIgnoreCase("NORMAL")) {
            return AlarmSeverity.NORMAL;
        }
        if (severity.equalsIgnoreCase("MINOR")) {
            return AlarmSeverity.MINOR;
        }
        if (severity.equalsIgnoreCase("MAJOR")) {
            return AlarmSeverity.MAJOR;
        }
        if (severity.equalsIgnoreCase("WARNING")) {
            return AlarmSeverity.WARNING;
        }
        if (severity.equalsIgnoreCase("CRITICAL")) {
            return AlarmSeverity.CRITICAL;
        }
        throw new IllegalArgumentException("CAnnot convert severity of %s to internal severity.".formatted(severity));
    }

}

