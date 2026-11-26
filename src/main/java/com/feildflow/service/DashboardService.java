package com.feildflow.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.feildflow.dto.DashboardResponse;
import com.feildflow.repository.AssignmentRepository;
import com.feildflow.repository.ServiceRequestRepository;
import com.feildflow.repository.WorkOrderRepository;
@Service
public class DashboardService {

    private final ServiceRequestRepository serviceRequestRepository;
    private final WorkOrderRepository workOrderRepository;
    private final AssignmentRepository assignmentRepository;

    public DashboardService(
            ServiceRequestRepository serviceRequestRepository,
            WorkOrderRepository workOrderRepository,
            AssignmentRepository assignmentRepository) {

        this.serviceRequestRepository = serviceRequestRepository;
        this.workOrderRepository = workOrderRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @Cacheable(value = "dashboardSummary", key = "'summary'")
    public DashboardResponse getDashboardSummary() {

        DashboardResponse response = new DashboardResponse();

        response.setTotalRequests(serviceRequestRepository.count());
        response.setTotalWorkOrders(workOrderRepository.count());
        response.setTotalAssignments(assignmentRepository.count());

        return response;
    }
}