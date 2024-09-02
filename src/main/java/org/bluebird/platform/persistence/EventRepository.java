package org.bluebird.platform.persistence;

import org.bluebird.platform.domain.model.EventDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<EventDTO, UUID> {
}
