package org.bluebird.platform.engine.alarms.definition.providers;

import org.bluebird.platform.engine.alarms.definition.AlarmDefinition;

import java.util.List;

public interface AlarmDefinitionProvider {
    List<AlarmDefinition> getAlarmDefinitions();

}
