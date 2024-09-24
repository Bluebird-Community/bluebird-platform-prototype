package org.bluebird.integrations.github;

import org.bluebird.platform.domain.alarms.AlarmDefinition;
import org.bluebird.platform.domain.alarms.AlarmDefinitionProvider;
import org.bluebird.platform.domain.conditions.EventConditions;
import org.bluebird.platform.domain.model.AlarmSeverity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GithubActionAlarmDefinitionProvider implements AlarmDefinitionProvider {
    @Override
    public List<AlarmDefinition> getAlarmDefinitions() {
        return List.of(
                AlarmDefinition.newBuilder()
                        .withNamespace("github.com/payloads/workflow_run")
                        .withDescription("Raises an alarm, if the given workflow failed")
                        .withLabel("Workflow failed")
                        .withSeverity(AlarmSeverity.MAJOR)
                        .withRaiseCondition(
                                EventConditions.and(
                                        EventConditions.matches("workflow_run.status", "completed"),
                                        EventConditions.not(EventConditions.matches("workflow_run.conclusion", "success"))
                                )
                        )
                        .withClearCondition(
                                EventConditions.and(
                                        EventConditions.matches("workflow_run.status", "completed"),
                                        EventConditions.matches("workflow_run.conclusion", "success")
                                )
                        )
                        .build()
        );
    }
}