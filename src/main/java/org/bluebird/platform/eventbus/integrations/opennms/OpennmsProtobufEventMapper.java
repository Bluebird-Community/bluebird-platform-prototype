package org.bluebird.platform.eventbus.integrations.opennms;

import lombok.AllArgsConstructor;
import org.bluebird.platform.api.events.definitions.EventDefinitionRenderer;
import org.bluebird.platform.api.events.definitions.EventRenderContext;
import org.bluebird.platform.api.events.definitions.OpennmsEventDefinitionProvider;
import org.bluebird.platform.engine.events.EventDTO;
import org.bluebird.platform.integration.opennms.protobuf.model.OpennmsModelProtos;
import org.bluebird.platform.persistence.model.EventLogEntity;
import org.bluebird.platform.persistence.model.EventLogParameterEntity;
import org.bluebird.platform.persistence.model.EventLogRef;
import org.bluebird.platform.persistence.model.NodeCriteria;
import org.bluebird.platform.persistence.model.SnmpInfo;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

/**
 * This class maps an {@link OpennmsModelProtos.Event} to the internal representation {@link EventLogParameterEntity}.
 */
@Component
@AllArgsConstructor
public class OpennmsProtobufEventMapper {

    private final OpennmsEventDefinitionProvider eventDefinitionProvider;

    public EventDTO<?> map(OpennmsModelProtos.Event event) {
        final var eventDefinition = eventDefinitionProvider.findEventDefinitionByUei(event.getUei());
        final var dto = new EventDTO<>()
                .withNamespace("opennms")
                .withRef(Long.toString(event.getId()))
                .withUei(event.getUei())
                .withSource(event.getSource())
//                .withTime(event.getTime() == 0 ? null : toLocalDateTime(event.getTime()))
                .withCreationTime(event.getCreateTime() == 0 ? null : toLocalDateTime(event.getCreateTime()))
                .withProperties(
                        Map.of(
                                "label", event.getLabel().substring(0, Math.min(event.getLabel().length(), 255)),
                                "message", event.getLogMessage().substring(0, Math.min(event.getLogMessage().length(), 255)),
                                "description", event.getDescription().substring(0, Math.min(event.getDescription().length(), 255)),
                                "display", Boolean.toString(event.getDisplay()),
                                "log", Boolean.toString(event.getLog()))
                );
                // TODO MVR add event params
                // TODO MVR add inventory ref
                // TODO MVR add payload

        // Populate key
        return eventDefinition
                .filter(it -> it.getAlarmData() != null)
                .filter(it -> it.getAlarmData().getAlarmType() <= 2)
                .map(definition -> {
                    final var alarmData = definition.getAlarmData();
                    final var type = alarmData.getAlarmType();
                    final var keyTemplate = type == 1 ? alarmData.getReductionKey() : alarmData.getClearKey();
                    final var renderer = new EventDefinitionRenderer(new EventRenderContext(dto));
                    final var key = renderer.render(keyTemplate);
                    return dto.withConsolidationKey(key);
                }).orElse(dto);
    }

//    public EventLogEntity map(OpennmsModelProtos.Event event) {
//        final var entity = new EventLogEntity()
//                .withDescription(event.getDescription().isBlank() ? null : event.getDescription().trim())
//                .withLabel(event.getLabel().isBlank() ? null : event.getLabel().trim())
//                .withUei(event.getUei().isBlank() ? null : event.getUei().trim())
//                .withMessage(event.getLogMessage().isBlank() ? null : event.getLogMessage().trim())
//                .withSource(event.getSource().isBlank() ? null : event.getSource().trim())
//                .withIpAddress(event.getIpAddress().isBlank() ? null : event.getIpAddress().trim())
//                .withDistPoller(event.getDistPoller().isBlank() ? null : event.getDistPoller().trim())
//                .withTime(event.getTime() == 0 ? null : toLocalDateTime(event.getTime()))
//                .withLog(event.getLog())
//                .withDisplay(event.getDisplay())
//                .withCreationTime(event.getCreateTime() == 0 ? null : toLocalDateTime(event.getCreateTime()))
//                .withSnmpInfo(map(event.getSnmpInfo()))
//                .withNodeCriteria(map(event.getNodeCriteria()));
//        if (event.getId() != 0) {
//            entity.setRef(EventLogRef.of("opennms", event.getId()));
//        }
//        final var entities = event.getParameterList().stream().map(this::map).toList();
//        entity.setParameters(entities);
//        return entity;
//    }

    private LocalDateTime toLocalDateTime(long millis) {
        return Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    private EventLogParameterEntity map(OpennmsModelProtos.EventParameter eventParameter) {
        return new EventLogParameterEntity()
                .withName(eventParameter.getName())
                .withType(eventParameter.getType())
                .withValue(eventParameter.getValue());
    }

    private NodeCriteria map(OpennmsModelProtos.NodeCriteria nodeCriteria) {
        if (nodeCriteria == null) return null;
        return new NodeCriteria()
                .withForeignId(nodeCriteria.getForeignId().isBlank() ? null : nodeCriteria.getForeignId().trim())
                .withLocation(nodeCriteria.getLocation().isBlank() ? null : nodeCriteria.getLocation().trim())
                .withId(nodeCriteria.getId() == 0 ? null : nodeCriteria.getId())
                .withNodeLabel(nodeCriteria.getNodeLabel().isBlank() ? null : nodeCriteria.getNodeLabel().trim())
                .withForeignSource(nodeCriteria.getForeignSource().isBlank() ? null : nodeCriteria.getForeignSource().trim());
    }

    public SnmpInfo map(OpennmsModelProtos.SnmpInfo info) {
        if (info == null) return null;
        return new SnmpInfo()
                .withCommunity(info.getCommunity().isBlank() ? null : info.getCommunity().trim())
                .withVersion(info.getVersion().isBlank() ? null : info.getVersion().trim())
                .withGeneric(info.getGeneric() == 0 ? null : info.getGeneric())
                .withSpecific(info.getSpecific() == 0 ? null : info.getSpecific())
                .withTrap_oid(info.getTrapOid().isBlank() ? null : info.getTrapOid().trim());
    }
}
