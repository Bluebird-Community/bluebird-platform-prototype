package org.bluebird.integrations.opennms.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpennmsEventRepository extends JpaRepository<OpennmsEventEntity, Integer> {
}
