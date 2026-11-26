
package com.feildflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feildflow.entity.ServiceRequest;
import com.feildflow.entity.ServiceRequestStatus;

public interface ServiceRequestRepository
        extends JpaRepository<ServiceRequest, Long> {

    List<ServiceRequest> findByStatus(ServiceRequestStatus status);

    List<ServiceRequest> findByRequesterId(Long requesterId);

    List<ServiceRequest> findByZoneId(Long zoneId);
    
    List<ServiceRequest> findAllByOrderByCreatedAtDesc();
}
