package org.bluebird.integrations.opennms.alarms;

import org.bluebird.integrations.opennms.events.xml.XmlEvent;
import org.bluebird.platform.domain.conditions.Condition;
import org.bluebird.platform.domain.conditions.Conditions;
import org.bluebird.platform.domain.conditions.EventConditions;
import org.bluebird.platform.domain.model.EventDTO;

public final class OpennmsRaiseConditions {
    private OpennmsRaiseConditions() {

    }

    public static Condition<EventDTO> matchesEvent(XmlEvent eventDefinition) {
        final var condition = EventConditions.matchesUei(eventDefinition.getUei());
        return Conditions.wrap(condition, (it) -> String.format("Event: %s", it));
    }
}
