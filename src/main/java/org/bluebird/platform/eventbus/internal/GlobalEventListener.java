package org.bluebird.platform.eventbus.internal;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bluebird.platform.domain.model.EventDTO;
import org.bluebird.platform.persistence.EventRepository;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class GlobalEventListener {

    private final EventRepository eventRepository;

    @EventListener
    @Order(Integer.MIN_VALUE)
    public void onEvent(EventDTO eventDTO) {
        eventRepository.save(eventDTO);
    }


}
