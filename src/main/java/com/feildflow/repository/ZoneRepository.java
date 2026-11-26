package com.feildflow.repository;

import com.feildflow.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ZoneRepository extends JpaRepository<Zone, Long> {

    List<Zone> findByActiveTrue();

    boolean existsByZoneName(String zoneName);
}