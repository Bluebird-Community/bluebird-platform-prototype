package org.bluebird.platform.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import org.bluebird.platform.engine.alarms.AlarmStateEnum;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name="zzz_alarms")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
public class AlarmEntity {
    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private AlarmStateEnum state = AlarmStateEnum.PENDING;

    @Column(columnDefinition = "TEXT")
    private String reductionKey;

    @Column(columnDefinition = "TEXT")
    private String clearKey;

    @Column(columnDefinition = "TEXT")
    private String eventUei;

    private Integer count = 1;

    @Column(columnDefinition = "TEXT")
    private String eventDistPoller;

    private LocalDateTime eventFirstTime;

    private LocalDateTime eventLastTime;

    @Column(columnDefinition = "TEXT")
    private String eventDescription;

    @Column(columnDefinition = "TEXT")
    private String eventMessage;

    private NodeCriteria eventNodeCriteria;

    private String clearedBy;

    private LocalDateTime clearedAt;

    @Version
    private Integer version;

    public boolean isCleared() {
        return state == AlarmStateEnum.CLEARED;
    }

    public boolean isPending() {
        return state == AlarmStateEnum.PENDING;
    }
}
