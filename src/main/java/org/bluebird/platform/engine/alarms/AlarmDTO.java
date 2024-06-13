package org.bluebird.platform.engine.alarms;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import org.bluebird.platform.engine.events.EventRef;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "alarms")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
public class AlarmDTO {
    @Id
    private UUID id;

    private String consolidationKey;

    private int count;

    private Integer level = 1;

    @Enumerated(EnumType.STRING)
    private AlarmSeverity severity;

    @Enumerated(EnumType.STRING)
    private AlarmStateEnum state = AlarmStateEnum.PENDING;

    @Embedded
    private EventRef eventRef;

    private String clearedBy;

    private LocalDateTime clearedAt;

    public boolean isCleared() {
        return state == AlarmStateEnum.CLEARED;
    }
}
