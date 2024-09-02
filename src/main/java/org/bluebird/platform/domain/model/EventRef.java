package org.bluebird.platform.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record EventRef(@Column(name = "ref_namespace") String namespace, @Column(name = "ref_id") String id) {

}
