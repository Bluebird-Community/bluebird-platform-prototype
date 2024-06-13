package org.bluebird.platform.api.events.definitions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bluebird.platform.engine.events.EventDTO;

@AllArgsConstructor
@Getter
public class EventRenderContext {
    private final EventDTO event;
}
