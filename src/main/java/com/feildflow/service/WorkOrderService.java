package com.feildflow.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.feildflow.dto.CreateWorkOrderRequest;
import com.feildflow.dto.WorkOrderResponse;
import com.feildflow.entity.Role;
import com.feildflow.entity.ServiceRequest;
import com.feildflow.entity.ServiceRequestStatus;
import com.feildflow.entity.User;
import com.feildflow.entity.WorkOrder;
import com.feildflow.entity.WorkOrderStatus;
import com.feildflow.repository.ServiceRequestRepository;
import com.feildflow.repository.UserRepository;
import com.feildflow.repository.WorkOrderRepository;

@Service
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    public WorkOrderService(
            WorkOrderRepository workOrderRepository,
            ServiceRequestRepository serviceRequestRepository,
            UserRepository userRepository,
            ActivityLogService activityLogService) {

        this.workOrderRepository = workOrderRepository;
        this.serviceRequestRepository = serviceRequestRepository;
        this.userRepository = userRepository;
        this.activityLogService = activityLogService;
    }

    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public WorkOrderResponse  createWorkOrder(
        CreateWorkOrderRequest request,
        Long plannerId) {

        ServiceRequest serviceRequest = serviceRequestRepository
                .findById(request.getServiceRequestId())
                .orElseThrow(() -> new RuntimeException("Service request not found"));

        if (serviceRequest.getStatus() != ServiceRequestStatus.APPROVED) {
            throw new RuntimeException("Only approved requests can create work orders");
        }

        if (workOrderRepository.existsByServiceRequestId(serviceRequest.getId())) {
            throw new RuntimeException("Work order already exists for this request");
        }

        User planner = userRepository.findById(plannerId)
                .orElseThrow(() -> new RuntimeException("Planner not found"));

        WorkOrder workOrder = new WorkOrder();

        workOrder.setServiceRequest(serviceRequest);
        workOrder.setTitle(serviceRequest.getTitle());
        workOrder.setInstructions(request.getInstructions());
        workOrder.setWorkOrderNumber(request.getWorkOrderNumber());
        workOrder.setStatus(WorkOrderStatus.CREATED);

        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);

        activityLogService.logActivity(
                planner,
                "WORK_ORDER",
                savedWorkOrder.getId(),
                "CREATED",
                "Work order created for service request " + serviceRequest.getId()
        );

        return mapToResponse(savedWorkOrder);
    }
    private WorkOrderResponse mapToResponse(WorkOrder workOrder) {

        WorkOrderResponse response = new WorkOrderResponse();

        response.setId(workOrder.getId());
        response.setWorkOrderNumber(workOrder.getWorkOrderNumber());
        response.setTitle(workOrder.getTitle());
        response.setInstructions(workOrder.getInstructions());
        response.setStatus(workOrder.getStatus().name());
        response.setServiceRequestId(
                workOrder.getServiceRequest().getId()
        );
        response.setCreatedAt(workOrder.getCreatedAt());

        return response;
    }   
    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public WorkOrderResponse verifyWorkOrder(Long workOrderId, Long supervisorId) {

        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new RuntimeException("Work order not found"));

        if (workOrder.getStatus() != WorkOrderStatus.COMPLETED) {
            throw new RuntimeException("Only completed work orders can be verified");
        }

        User supervisor = userRepository.findById(supervisorId)
                .orElseThrow(() -> new RuntimeException("Supervisor not found"));

        if (supervisor.getRole() != Role.SUPERVISOR) {
            throw new RuntimeException("Only supervisors can verify work orders");
        }

        workOrder.setStatus(WorkOrderStatus.VERIFIED);

        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);

        activityLogService.logActivity(
                supervisor,
                "WORK_ORDER",
                workOrder.getId(),
                "VERIFIED",
                "Work order verified by supervisor"
        );

        return mapToResponse(savedWorkOrder);
    }
    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public WorkOrderResponse closeWorkOrder(Long workOrderId, Long supervisorId) {

        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new RuntimeException("Work order not found"));

        if (workOrder.getStatus() != WorkOrderStatus.VERIFIED) {
            throw new RuntimeException("Only verified work orders can be closed");
        }

        User supervisor = userRepository.findById(supervisorId)
                .orElseThrow(() -> new RuntimeException("Supervisor not found"));

        if (supervisor.getRole() != Role.SUPERVISOR) {
            throw new RuntimeException("Only supervisors can close work orders");
        }

        workOrder.setStatus(WorkOrderStatus.CLOSED);
        workOrderRepository.save(workOrder);

        activityLogService.logActivity(
                supervisor,
                "WORK_ORDER",
                workOrder.getId(),
                "CLOSED",
                "Work order closed by supervisor"
        );

        return mapToResponse(workOrder);
    }
}