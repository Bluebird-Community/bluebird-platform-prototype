package org.bluebird.platform.persistence.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.ManyToAny;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "zzz_event_logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
public class EventLogEntity {
    @Id
    // TODO MVR figure out how to populate this field automatically
    private UUID id;

    @Column(name = "uei", length = 256)
    private String uei;

    @Column(name = "label", length = 256)
    private String label;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    private String source;

    private boolean log;

    private boolean display;

    private NodeCriteria nodeCriteria;

    private String ipAddress;

    private String distPoller;

    private SnmpInfo snmpInfo;

    private EventLogRef ref;

    private LocalDateTime time;

    private LocalDateTime creationTime;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "eventLog")
    private List<EventLogParameterEntity> parameters = new ArrayList<>();

}
