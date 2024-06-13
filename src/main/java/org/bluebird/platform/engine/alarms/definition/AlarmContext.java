package org.bluebird.platform.engine.alarms.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bluebird.platform.engine.alarms.AlarmDTO;
import org.bluebird.platform.engine.events.EventDTO;

@Getter
@AllArgsConstructor
public class AlarmContext {
    private final EventDTO<?> event;
    private final AlarmDTO alarm;

}
