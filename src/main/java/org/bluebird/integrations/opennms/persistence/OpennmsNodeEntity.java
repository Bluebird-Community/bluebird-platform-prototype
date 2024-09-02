package org.bluebird.integrations.opennms.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import org.bluebird.platform.domain.model.NodeCriteria;

@Entity
@Table(name = "node")
@Getter
@Setter
public class OpennmsNodeEntity {
    @Id
    @Column(name = "nodeid")
    private Long id;

    @Column(name = "location")
    private String location;

    @Column(name = "nodelabel")
    private String label;

    @Column(name = "foreignsource")
    private String foreignSource;

    @Column(name = "foreignid")
    private String foreignId;

    @Transient
    public NodeCriteria getNodeCriteria() {
        return new NodeCriteria(id, foreignSource, foreignId, label, location);
    }
}
