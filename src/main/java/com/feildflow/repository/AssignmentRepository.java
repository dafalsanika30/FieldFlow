
package com.feildflow.repository;

import com.feildflow.entity.Assignment;
import com.feildflow.entity.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByWorkOrderId(Long workOrderId);

    List<Assignment> findByTechnicianId(Long technicianId);

    List<Assignment> findByTechnicianIdAndStatus(
            Long technicianId, AssignmentStatus status);

    Optional<Assignment> findFirstByWorkOrderIdAndStatusOrderByAssignedAtDesc(
            Long workOrderId, AssignmentStatus status);
}
