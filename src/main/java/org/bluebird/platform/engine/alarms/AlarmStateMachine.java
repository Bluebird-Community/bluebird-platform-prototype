package org.bluebird.platform.engine.alarms;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.bluebird.platform.api.events.definitions.EventDefinitionRenderer;
import org.bluebird.platform.api.events.definitions.EventRenderContext;
import org.bluebird.platform.engine.alarms.definition.AlarmContext;
import org.bluebird.platform.engine.alarms.definition.AlarmDefinition;
import org.bluebird.platform.engine.events.EventDTO;
import org.bluebird.platform.persistence.repository.AlarmRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Component
public class AlarmStateMachine {

    private final AlarmRepository alarmRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void handle(AlarmDefinition alarmDefinition, EventDTO<?> event) {
        final var isRaising = alarmDefinition.getRaiseCondition().matches(event);
        final var alarmOptional = alarmRepository.findByConsolidationKey(event.getConsolidationKey());
        if (isRaising || alarmOptional.isPresent()) {
            if (alarmOptional.isEmpty()) {
                handleCreate(alarmDefinition, event);
            } else {
                handleUpdate(alarmDefinition, event, alarmOptional.get());
            }
        }
    }

    private void handleCreate(AlarmDefinition alarmDefinition, EventDTO<?> event) {
        final var alarm = createAlarm(event, alarmDefinition);
        final var reductionKey = new EventDefinitionRenderer(new EventRenderContext(event)).render(alarmDefinition.getRaiseKey());
        alarm.setConsolidationKey(reductionKey);
        alarmRepository.save(alarm);
        eventPublisher.publishEvent(createEventFrom(event, alarm));
    }

    private EventDTO<?> createEventFrom(EventDTO<?> origin, AlarmDTO alarm) {
        return new EventDTO<>()
                .withId(UUID.randomUUID()) // TODO MVR this must be done by the database
                .withUei("alarmPropagation/%s".formatted(origin.getUei()))
                .withNamespace("internal")
                .withRef(alarm.getId().toString())
                .withSource(getClass().getSimpleName())
                .withConsolidationKey("alarmPropagation/%s/level=%s:%s".formatted(alarm.getConsolidationKey(), alarm.getLevel(), alarm.getLevel() + 1))
                .withLevel(alarm.getLevel());
    }

    private void handleUpdate(AlarmDefinition alarmDefinition, EventDTO<?> event, AlarmDTO alarm) {
        if (alarm.getState() == null) {
            throw new IllegalStateException("State must not be null");
        }
        // Always update last event data
//        alarm.setEventLastTime(event.getTime());
//        alarm.setEvent(event); // TODO MVR

        // Can Resolve
        if (alarmDefinition.getClearCondition().matches(event)) {
            if (alarm.isCleared()) { // already resolved
                alarm.setCount(alarm.getCount() + 1); // increment counter
            } else { // resolve
                alarm.setState(AlarmStateEnum.CLEARED);
                alarm.setClearedBy("event");
                alarm.setClearedAt(LocalDateTime.now());

                final var resolvingPropagateEvent = createEventFrom(event, alarm);
                eventPublisher.publishEvent(resolvingPropagateEvent);
            }
        } else {
            alarm.setCount(alarm.getCount() + 1); // count the occurance
            if (alarm.getCount() == 2) {
                // TODO MVR reset severity
            }
        }
    }

    private static AlarmDTO createAlarm(EventDTO<?> event, final AlarmDefinition alarmDefinition) {
        // TODO MVR not all properties from opennms were taken into account
        return new AlarmDTO()
                .withId(UUID.randomUUID()) // TODO MVR this must be performed by the database
                .withConsolidationKey(event.getConsolidationKey())
                .withCount(1)
                .withLevel(alarmDefinition.getLevel())
                .withSeverity(alarmDefinition.getSeverity())
                .withEventRef(event.asRef());
    }
}
