package org.bluebird.platform.engine.alarms.definition.providers;


import org.bluebird.platform.engine.alarms.AlarmSeverity;
import org.bluebird.platform.engine.alarms.definition.AlarmDefinition;
import org.bluebird.platform.engine.alarms.definition.ConsolidationKeyProviders;
import org.bluebird.platform.engine.alarms.definition.conditions.EventConditions;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestAlarmDefinitionProvider implements AlarmDefinitionProvider {

    @Override
    public List<AlarmDefinition> getAlarmDefinitions() {
        final var keyTemplate = "%key%";
        return List.of(
                AlarmDefinition.newBuilder()
                        .withDescription("Dummy description")
                        .withUei("alarmPropagation/uei.opennms.org/nodes/nodeDown")
                        .withLevel(2)
                        .withSeverity(AlarmSeverity.MAJOR)
                        .withRaise(
                                EventConditions.matchesUei("alarmPropagation/uei.opennms.org/nodes/nodeDown"),
                                ConsolidationKeyProviders.template(keyTemplate)
                        )
                        .withClear(
                                EventConditions.matchesUei("alarmPropagation/uei.opennms.org/nodes/nodeUp"),
                                ConsolidationKeyProviders.template(keyTemplate)
                        ).build(),
                AlarmDefinition.newBuilder()
                        .withDescription("Dummy description 2")
                        .withUei("alarmPropagation/alarmPropagation/uei.opennms.org/nodes/nodeDown")
                        .withLevel(3)
                        .withSeverity(AlarmSeverity.CRITICAL)
                        .withRaise(
                                EventConditions.matchesUei("alarmPropagation/alarmPropagation/uei.opennms.org/nodes/nodeDown"),
                                ConsolidationKeyProviders.template(keyTemplate)
                        )
//                        .withClear(
//                                EventConditions.matchesUei("alarmPropagation/alarmPropagation/uei.opennms.org/nodes/nodeUp"),
//                                ConsolidationKeyProviders.template(keyTemplate)
//                        )
                        .build());
    }
}
