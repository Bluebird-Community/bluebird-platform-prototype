package org.bluebird.platform.api.events.definitions;

import lombok.AllArgsConstructor;
import org.opennms.integration.xml.eventconf.events.xml.XmlAlarmData;
import org.opennms.integration.xml.eventconf.events.xml.XmlEvent;

import java.util.Objects;

@AllArgsConstructor
public class EventDefinition {
    private final XmlEvent definition;
    private final EventDefinitionRenderer renderer;

    public XmlAlarmData getAlarmData() {
        return Objects.requireNonNull(definition.getAlarmData());
    }

    public boolean canResolveAlarm() {
        return getAlarmData().getAlarmType() == 2;
    }

    public boolean canRaiseAlarm() {
        return getAlarmData().getAlarmType() == 1;
    }

    public AlarmKeys getKeys() {
        Objects.requireNonNull(renderer);
        return AlarmKeys.of(renderer.render(getAlarmData().getReductionKey()),
                renderer.render(getAlarmData().getClearKey()));
    }

    public AlarmKeys getRawKeys() {
        return AlarmKeys.of(getAlarmData().getReductionKey(), getAlarmData().getClearKey());
    }
}
