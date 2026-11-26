package com.feildflow.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feildflow.entity.WorkOrder;
import com.feildflow.entity.WorkOrderStatus;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long>{
    Optional<WorkOrder> findByWorkOrderNumber(String workOrderNumber);

    List<WorkOrder> findByStatus(WorkOrderStatus status);

    Optional<WorkOrder> findByServiceRequestId(Long requestId);
    boolean existsByServiceRequestId(Long serviceRequestId);
    boolean existsByWorkOrderNumber(String workOrderNumber);
}
