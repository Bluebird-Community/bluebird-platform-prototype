package org.bluebird.platform.domain.alarms;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.bluebird.platform.domain.model.AlarmDTO;
import org.bluebird.platform.domain.model.AlarmStateEnum;
import org.bluebird.platform.domain.model.EventDTO;
import org.bluebird.platform.persistence.AlarmRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@AllArgsConstructor
public class AlarmStateMachine {

    private final AlarmRepository alarmRepository;
    private final ApplicationEventPublisher eventPublisher;

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
        final var reductionKey = event.getConsolidationKey();
        alarm.setConsolidationKey(reductionKey);
        alarmRepository.save(alarm);
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
