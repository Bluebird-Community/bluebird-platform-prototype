package org.bluebird.platform.persistence.repository;

import org.bluebird.platform.engine.alarms.AlarmDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlarmRepository extends JpaRepository<AlarmDTO, UUID> {

    @Query("select a from AlarmDTO a where a.consolidationKey = :consolidationKey")
    Optional<AlarmDTO> findByConsolidationKey(String consolidationKey);
}
