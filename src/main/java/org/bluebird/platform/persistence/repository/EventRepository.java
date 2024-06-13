package org.bluebird.platform.persistence.repository;

import org.bluebird.platform.engine.events.EventDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<EventDTO<?>, UUID> {
}
