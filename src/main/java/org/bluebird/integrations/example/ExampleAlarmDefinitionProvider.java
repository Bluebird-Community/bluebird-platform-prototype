package org.bluebird.integrations.example;


import org.bluebird.platform.domain.alarms.AlarmDefinition;
import org.bluebird.platform.domain.alarms.AlarmDefinitionProvider;
import org.bluebird.platform.domain.model.AlarmSeverity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExampleAlarmDefinitionProvider implements AlarmDefinitionProvider {

    // TODO MVR verify that this still works as expected
    @Override
    public List<AlarmDefinition> getAlarmDefinitions() {
        return List.of(
                AlarmDefinition.newBuilder()
                        .withDescription("Example description")
                        .withNamespace("alarmPropagation/uei.opennms.org/nodes/nodeDown")
                        .withLevel(2)
                        .withSeverity(AlarmSeverity.MAJOR)
                        .build(),
                AlarmDefinition.newBuilder()
                        .withDescription("Dummy description 2")
                        .withNamespace("alarmPropagation/alarmPropagation/uei.opennms.org/nodes/nodeDown")
                        .withLevel(3)
                        .withSeverity(AlarmSeverity.CRITICAL)
//                        .withClear(
//                                EventConditions.matchesUei("alarmPropagation/alarmPropagation/uei.opennms.org/nodes/nodeUp"),
//                                ConsolidationKeyProviders.template(keyTemplate)
//                        )
                        .build());
    }
}
