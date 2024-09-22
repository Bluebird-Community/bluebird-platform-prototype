package org.bluebird.integrations.opennms.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "service")
@Getter
@Setter
public class OpennmsServiceEntity {
    @Id
    @Column(name = "serviceid")
    private Integer id;

    @Column(name = "servicename", length = 255)
    private String name;
}
