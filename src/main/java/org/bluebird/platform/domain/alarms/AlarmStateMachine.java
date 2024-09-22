package org.bluebird.platform.domain.alarms;

import jakarta.transaction.Transactional;
import org.bluebird.platform.domain.consolidationkey.ConsolidationKeyRenderer;
import org.bluebird.platform.domain.model.AlarmDTO;
import org.bluebird.platform.domain.model.AlarmStateEnum;
import org.bluebird.platform.domain.model.EventDTO;
import org.bluebird.platform.persistence.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class AlarmStateMachine {

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    // TODO MVR ...
    @Autowired
    private ConsolidationKeyRenderer consolidationKeyRenderer;

    // TODO MVR let's disable this for now and see if we actually need this
    @Value("${org.bluebird.alarm.propagation.enabled}")
    private Boolean propagateAlarms;

//    @Autowired
//    private OpennmsEventTemplateRenderer templateRenderer;

    @Transactional
    public void handle(AlarmDefinition alarmDefinition, EventDTO event) {
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

    private void handleCreate(AlarmDefinition alarmDefinition, EventDTO event) {
        final var alarm = createAlarm(event, alarmDefinition);
        // TODO MVR this should be more dynamic and be delegated to the type or source of the event instead
        final var reductionKey = consolidationKeyRenderer.render(alarmDefinition.getRaiseKey(), event);
        alarm.setConsolidationKey(reductionKey);
        alarmRepository.save(alarm);
        if (propagateAlarms) {
            eventPublisher.publishEvent(createEventFrom(event, alarm));
        }
    }

    private EventDTO createEventFrom(EventDTO origin, AlarmDTO alarm) {
        return new EventDTO()
                .withId(UUID.randomUUID()) // TODO MVR this must be done by the database
                .withUei("alarmPropagation/%s".formatted(origin.getUei()))
                .withNamespace("internal")
                .withRef(alarm.getId().toString())
                .withSource(getClass().getSimpleName())
                .withConsolidationKey("alarmPropagation/%s/level=%s:%s".formatted(alarm.getConsolidationKey(), alarm.getLevel(), alarm.getLevel() + 1));
    }

    private void handleUpdate(AlarmDefinition alarmDefinition, EventDTO event, AlarmDTO alarm) {
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

                if (propagateAlarms) {
                    final var resolvingPropagateEvent = createEventFrom(event, alarm);
                    eventPublisher.publishEvent(resolvingPropagateEvent);
                }
            }
        } else {
            alarm.setCount(alarm.getCount() + 1); // count the occurance
            if (alarm.getCount() == 2) {
                // TODO MVR reset severity
            }
        }
    }

    private static AlarmDTO createAlarm(EventDTO event, final AlarmDefinition alarmDefinition) {
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
