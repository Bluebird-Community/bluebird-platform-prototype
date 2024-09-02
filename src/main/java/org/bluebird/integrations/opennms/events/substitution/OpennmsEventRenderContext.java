package org.bluebird.integrations.opennms.events.substitution;

import org.bluebird.integrations.opennms.persistence.OpennmsEventEntity;
import org.bluebird.platform.domain.model.EventDTO;

public record OpennmsEventRenderContext(EventDTO event, OpennmsEventEntity entity) {
}
