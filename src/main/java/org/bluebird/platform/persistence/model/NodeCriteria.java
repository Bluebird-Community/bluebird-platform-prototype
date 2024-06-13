package org.bluebird.platform.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import java.math.BigInteger;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@With
public class NodeCriteria {
    @Column(name = "node_id")
    private Long id;

    @Column(name = "node_foreign_source")
    private String foreignSource;

    @Column(name = "node_foreign_id")
    private String foreignId;

    @Column(name = "node_label")
    private String nodeLabel;

    @Column(name = "node_location")
    private String location;
}
