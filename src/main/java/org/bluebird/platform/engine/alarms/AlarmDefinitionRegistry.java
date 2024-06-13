package org.bluebird.platform.engine.alarms;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bluebird.platform.engine.alarms.definition.AlarmDefinition;
import org.bluebird.platform.engine.alarms.definition.providers.AlarmDefinitionProvider;
import org.bluebird.platform.engine.events.EventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class AlarmDefinitionRegistry {

    private final List<AlarmDefinitionProvider> providers;

    @PostConstruct
    public void init() {
        // TODO MVR ...
        final var alarmDefinitionLog = providers.stream()
                .flatMap(it -> it.getAlarmDefinitions().stream())
                .sorted(Comparator.comparing(AlarmDefinition::getLevel).thenComparing(AlarmDefinition::getSeverity))
                .map(AlarmDefinition::getDescription).collect(Collectors.joining("\n\n\n"));
        log.info("Found the following alarm definitions:\n{}", alarmDefinitionLog);
    }

    public List<AlarmDefinition> findAlarmDefinitions(EventDTO<?> event) {
        return providers.stream()
                .flatMap(it -> it.getAlarmDefinitions().stream())
                .filter(it -> it.getClearCondition().matches(event) || it.getRaiseCondition().matches(event))
                // If an event matches both conditions it is not possible, so we ignore it for now
                .filter(it -> !(it.getRaiseCondition().matches(event) && it.getClearCondition().matches(event)))
                .sorted(Comparator.comparing(AlarmDefinition::getLevel).thenComparing(AlarmDefinition::getSeverity))
                .toList();
    }
}

