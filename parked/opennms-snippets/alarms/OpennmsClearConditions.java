package org.bluebird.integrations.opennms.alarms;

import org.bluebird.integrations.opennms.events.xml.XmlEvent;
import org.bluebird.platform.domain.conditions.Condition;
import org.bluebird.platform.domain.conditions.Conditions;
import org.bluebird.platform.domain.conditions.EventConditions;
import org.bluebird.platform.domain.model.EventDTO;

import java.util.Optional;

public final class OpennmsClearConditions {
    private OpennmsClearConditions() {

    }

    public static Condition<EventDTO> matchesEvent(Optional<XmlEvent> eventOptional) {
        return eventOptional
                .map(OpennmsClearConditions::matchesEvent)
                .orElse(EventConditions.none());
    }

    public static Condition<EventDTO> matchesEvent(XmlEvent event) {
        return Conditions.wrap(EventConditions.matchesUei(event.getUei()), (it) -> String.format("Event: %s", it));
    }
}
