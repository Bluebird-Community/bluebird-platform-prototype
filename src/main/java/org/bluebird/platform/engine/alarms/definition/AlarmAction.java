package org.bluebird.platform.engine.alarms.definition;

public interface AlarmAction {
    String getDescription();
    void apply(AlarmContext context);
}
