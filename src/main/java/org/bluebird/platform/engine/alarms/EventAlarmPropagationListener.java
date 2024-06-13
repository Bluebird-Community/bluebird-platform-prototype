package org.bluebird.platform.engine.alarms;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bluebird.platform.engine.events.EventDTO;
import org.bluebird.platform.persistence.repository.EventRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@AllArgsConstructor
@Component // TODO MVR verify component creation and lifecycle
@Transactional
public class EventAlarmPropagationListener {

    private final EventRepository eventRepository;
    private final AlarmDefinitionRegistry alarmDefinitionRegistry;
    private final AlarmStateMachine alarmStateMachine;

    @EventListener
    void onEvent(final EventDTO<?> event) {
        Objects.requireNonNull(event, "Cannot create alarm from null event.");
        if (shouldProcess(event)) {
            handle(event);
        }
    }

    private void handle(final EventDTO<?> event) {
        // TODO MVR this may be delegated to another service
        final var refreshedEvent = eventRepository.findById(event.getId());
        if (refreshedEvent.isPresent()) {
            final var alarmDefinitions = alarmDefinitionRegistry.findAlarmDefinitions(event);
            alarmDefinitions.forEach(alarmDefinition -> alarmStateMachine.handle(alarmDefinition, event));
        }
    }

    private static boolean shouldProcess(final EventDTO<?> event) {
        return
//                && Objects.equals(event.getProperties().get("log"), "true") // TODO MVR this is not handled correctly by protobuf producer
                event.getConsolidationKey() != null && !event.getConsolidationKey().isEmpty();
    }

}
