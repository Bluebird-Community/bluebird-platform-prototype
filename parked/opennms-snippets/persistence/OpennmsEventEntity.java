package org.bluebird.integrations.opennms.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "events")
@Getter
@Setter
public class OpennmsEventEntity {
    @Id
    @Column(name = "eventid")
    private Integer id;

    @Column(name = "eventuei")
    private String uei;

    @Column(name="eventdescr")
    private String description;

    @Column(name="eventlogmsg")
    private String eventlogmsg;

    @Column(name="ifindex")
    private String ifIndex;

    @Column(name="ipaddr")
    private String ipAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="nodeid")
    private OpennmsNodeEntity node;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="serviceid")
    private OpennmsServiceEntity service;



}
