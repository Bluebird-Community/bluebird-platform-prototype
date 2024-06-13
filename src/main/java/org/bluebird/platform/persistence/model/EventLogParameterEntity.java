package org.bluebird.platform.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Entity
@Table(name = "zzz_event_log_parameters")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@With
public class EventLogParameterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "value", columnDefinition = "TEXT")
    private String value;

    @Column(name = "type", length = 256)
    private String type;

    @ManyToOne
    @JoinColumn(name="event_id", nullable=false)
    private EventLogEntity eventLog;
}
