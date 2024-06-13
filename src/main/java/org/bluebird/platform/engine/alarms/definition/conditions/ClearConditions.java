package org.bluebird.platform.engine.alarms.definition.conditions;

import org.bluebird.platform.engine.events.EventDTO;
import org.opennms.integration.xml.eventconf.events.xml.XmlEvent;

import java.util.Optional;

public final class ClearConditions {
    private ClearConditions() {

    }

    public static Condition<EventDTO<?>> matchesEvent(Optional<XmlEvent> eventOptional) {
        return eventOptional
                .map(ClearConditions::matchesEvent)
                .orElse(EventConditions.none());
    }

    public static Condition<EventDTO<?>> matchesEvent(XmlEvent event) {
        return Conditions.wrap(EventConditions.matchesUei(event.getUei()), (it) -> String.format("Event: %s", it));
    }
}
