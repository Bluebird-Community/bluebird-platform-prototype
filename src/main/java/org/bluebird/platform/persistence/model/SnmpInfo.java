package org.bluebird.platform.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import java.math.BigInteger;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
public class SnmpInfo {

    @Column(name="snmp_info_id")
    private String id;

    @Column(name="snmp_info_version")
    private String version;

    @Column(name="snmp_info_specific")
    private Integer specific;

    @Column(name="snmp_info_generic")
    private Integer generic;

    @Column(name="snmp_info_community")
    private String community;

    @Column(name="snmp_info_trap_oid")
    private String trap_oid;
}
