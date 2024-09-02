package org.bluebird.platform.domain.alarms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bluebird.platform.domain.model.AlarmDTO;
import org.bluebird.platform.domain.model.EventDTO;

@Getter
@AllArgsConstructor
public class AlarmContext {
    private final EventDTO event;
    private final AlarmDTO alarm;

}
