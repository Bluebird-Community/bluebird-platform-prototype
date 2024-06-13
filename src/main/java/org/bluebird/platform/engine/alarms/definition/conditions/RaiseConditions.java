package org.bluebird.platform.engine.alarms.definition.conditions;

import org.bluebird.platform.engine.events.EventDTO;
import org.opennms.integration.xml.eventconf.events.xml.XmlEvent;

public final class RaiseConditions {
    private RaiseConditions() {

    }

    public static Condition<EventDTO<?>> matchesEvent(XmlEvent eventDefinition) {
        final var condition = EventConditions.matchesUei(eventDefinition.getUei());
        return Conditions.wrap(condition, (it) -> String.format("Event: %s", it));
    }
}
