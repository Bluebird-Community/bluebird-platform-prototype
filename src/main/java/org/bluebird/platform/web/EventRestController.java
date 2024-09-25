package org.bluebird.platform.web;

import lombok.AllArgsConstructor;
import org.bluebird.platform.domain.consolidationkey.ConsolidationKeyRenderer;
import org.bluebird.platform.domain.model.EventDTO;
import org.bluebird.platform.persistence.EventRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class EventRestController {

    private final ConsolidationKeyRenderer consolidationKeyRenderer;
    private final EventRepository eventRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @PostMapping("/events")
    void sendEvent(@RequestBody EventDTO event) {
        if (event.getConsolidationKey() != null) {
            final var renderedKey = consolidationKeyRenderer.render(event.getConsolidationKey(), event);
            event.setConsolidationKey(renderedKey);
        }
        // TODO MVR generate id automatically
        event.setId(UUID.randomUUID());
        eventRepository.save(event);
        applicationEventPublisher.publishEvent(event);
    }

}
