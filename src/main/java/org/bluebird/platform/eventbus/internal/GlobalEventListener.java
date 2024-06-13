package org.bluebird.platform.eventbus.internal;

import lombok.extern.slf4j.Slf4j;
import org.bluebird.platform.engine.events.EventDTO;
import org.bluebird.platform.persistence.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GlobalEventListener {

    @Autowired
    private EventRepository eventRepository;

    @EventListener
    @Order(Integer.MIN_VALUE)
    public void onEvent(EventDTO<?> eventDTO) {
        eventRepository.save(eventDTO);
    }


}
