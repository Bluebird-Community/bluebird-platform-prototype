package org.bluebird.integrations.opennms;

import org.bluebird.platform.domain.alarms.AlarmDefinition;
import org.bluebird.platform.domain.alarms.AlarmDefinitionProvider;
import org.bluebird.platform.domain.conditions.EventConditions;
import org.bluebird.platform.domain.model.AlarmSeverity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OpennmsAlarmDefinitionProvider implements AlarmDefinitionProvider {
    @Override
    public List<AlarmDefinition> getAlarmDefinitions() {
        return List.of(
                AlarmDefinition.newBuilder()
                        .withNamespace("uei.opennms.org/nodes")
                        .withLabel("OpenNMS-defined node alarm: nodeDown")
                        .withDescription("""
                                All interfaces on node {payload.parameters.values.nodeLabel} are
                                down because of the following condition: {payload.parameters.values.eventReason}.
                                This alarm is created when node outage processing determines
                                that all interfaces on the node are down.
                                New outage records have been created and service level
                                availability calculations will be impacted until this outage
                                is resolved
                                """)
                        .withRaiseCondition(EventConditions.matches("payload.uei", "uei.opennms.org/nodes/nodeDown"))
                        .withClearCondition(EventConditions.matches("payload.uei", "uei.opennms.org/nodes/nodeUp"))
                        .withSeverity(AlarmSeverity.MAJOR)
                        .build(),

                AlarmDefinition.newBuilder()
                        .withNamespace("uei.opennms.org/nodes")
                        .withLabel("OpenNMS-defined node alarm: interfaceDown")
                        .withDescription("""
                                All services are down on interface {payload.interface}.
                                This event is generated when node outage processing determines
                                that the critical service or all services on the interface are now down.
                                New outage records have been created and service level
                                availability calculations will be impacted until this outage
                                is resolved.
                                """)
                        .withRaiseCondition(EventConditions.matches("payload.uei", "uei.opennms.org/nodes/interfaceDown"))
                        .withClearCondition(EventConditions.matches("payload.uei", "uei.opennms.org/nodes/interfaceUp"))
                        .withSeverity(AlarmSeverity.MINOR)
                        .build(),

                AlarmDefinition.newBuilder()
                        .withNamespace("uei.opennms.org/nodes")
                        .withLabel("OpenNMS-defined node alarm: nodeLostService")
                        .withDescription("""
                                A {payload.service} outage was identified on interface
                                {payload.interface} because of the following condition: {payload.parameters.values.eventReason}.
                                A new Outage record has been created and service level
                                availability calculations will be impacted until this outage is
                                resolved.
                                """)
                        .withRaiseCondition(EventConditions.matches("payload.uei", "uei.opennms.org/nodes/nodeLostService"))
                        .withClearCondition(EventConditions.matches("payload.uei", "uei.opennms.org/nodes/nodeRegainedService"))
                        .withSeverity(AlarmSeverity.MINOR)
                        .build(),

                AlarmDefinition.newBuilder()
                        .withNamespace("uei.opennms.org/internal")
                        .withLabel("OpenNMS-defined internal alarm: reload specified daemon configuration failed")
                        .withDescription("""
                                The administrator has changed the daemon: {payload.parameters.values.daemonName}
                                configuration files and the request for the configuration to be re-marshaled and applied
                                has failed.
                                """)
                        .withRaiseCondition(EventConditions.matches("payload.uei", "uei.opennms.org/internal/reloadDaemonConfigFailed"))
                        .withClearCondition(EventConditions.matches("payload.uei", "uei.opennms.org/internal/reloadDaemonConfigSuccessful"))
                        .withSeverity(AlarmSeverity.MAJOR)
                        .build()
        );
    }
}
