package org.bluebird.integrations.dummy;


import org.bluebird.integrations.opennms.events.OpennmsConsolidationKeyProviders;
import org.bluebird.platform.domain.alarms.AlarmDefinition;
import org.bluebird.platform.domain.alarms.AlarmDefinitionProvider;
import org.bluebird.platform.domain.conditions.EventConditions;
import org.bluebird.platform.domain.model.AlarmSeverity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DummyAlarmDefinitionProvider implements AlarmDefinitionProvider {

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
                                OpennmsConsolidationKeyProviders.template(keyTemplate)
                        )
                        .withClear(
                                EventConditions.matchesUei("alarmPropagation/uei.opennms.org/nodes/nodeUp"),
                                OpennmsConsolidationKeyProviders.template(keyTemplate)
                        ).build(),
                AlarmDefinition.newBuilder()
                        .withDescription("Dummy description 2")
                        .withUei("alarmPropagation/alarmPropagation/uei.opennms.org/nodes/nodeDown")
                        .withLevel(3)
                        .withSeverity(AlarmSeverity.CRITICAL)
                        .withRaise(
                                EventConditions.matchesUei("alarmPropagation/alarmPropagation/uei.opennms.org/nodes/nodeDown"),
                                OpennmsConsolidationKeyProviders.template(keyTemplate)
                        )
//                        .withClear(
//                                EventConditions.matchesUei("alarmPropagation/alarmPropagation/uei.opennms.org/nodes/nodeUp"),
//                                ConsolidationKeyProviders.template(keyTemplate)
//                        )
                        .build());
    }
}
