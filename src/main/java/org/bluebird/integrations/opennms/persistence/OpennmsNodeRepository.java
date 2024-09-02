package org.bluebird.integrations.opennms.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpennmsNodeRepository extends JpaRepository<OpennmsNodeEntity, Long> {
}
