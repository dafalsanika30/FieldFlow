
package com.feildflow.repository;

import com.feildflow.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    List<ActivityLog> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(
            String entityType, Long entityId);

    List<ActivityLog> findByUserIdOrderByCreatedAtDesc(Long userId);
}
