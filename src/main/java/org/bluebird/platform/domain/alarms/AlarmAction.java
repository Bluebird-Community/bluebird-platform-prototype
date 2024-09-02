package org.bluebird.platform.domain.alarms;

public interface AlarmAction {
    String getDescription();
    void apply(AlarmContext context);
}
