package com.feildflow.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.feildflow.dto.AssignmentResponse;
import com.feildflow.dto.CreateAssignmentRequest;
import com.feildflow.dto.TechnicianActionRequest;
import com.feildflow.entity.Assignment;
import com.feildflow.entity.AssignmentStatus;
import com.feildflow.entity.Role;
import com.feildflow.entity.User;
import com.feildflow.entity.WorkOrder;
import com.feildflow.entity.WorkOrderStatus;
import com.feildflow.repository.AssignmentRepository;
import com.feildflow.repository.UserRepository;
import com.feildflow.repository.WorkOrderRepository;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final WorkOrderRepository workOrderRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    public AssignmentService(
            AssignmentRepository assignmentRepository,
            WorkOrderRepository workOrderRepository,
            UserRepository userRepository,
            ActivityLogService activityLogService) {

        this.assignmentRepository = assignmentRepository;
        this.workOrderRepository = workOrderRepository;
        this.userRepository = userRepository;
        this.activityLogService = activityLogService;
    }

    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public AssignmentResponse  assignTechnician(CreateAssignmentRequest request, Long plannerId) {

        WorkOrder workOrder = workOrderRepository.findById(request.getWorkOrderId())
                .orElseThrow(() -> new RuntimeException("Work order not found"));

        if (workOrder.getStatus() != WorkOrderStatus.CREATED) {
            throw new RuntimeException("Work order is not available for assignment");
        }

        User technician = userRepository.findById(request.getTechnicianId())
                .orElseThrow(() -> new RuntimeException("Technician not found"));

        if (technician.getRole() != Role.TECHNICIAN) {
            throw new RuntimeException("Selected user is not a technician");
        }

        User planner = userRepository.findById(plannerId)
                .orElseThrow(() -> new RuntimeException("Planner not found"));

        Assignment assignment = new Assignment();
        assignment.setWorkOrder(workOrder);
        assignment.setTechnician(technician);
        assignment.setStatus(AssignmentStatus.ASSIGNED);
        assignment.setRemarks(request.getRemarks());

        Assignment savedAssignment = assignmentRepository.save(assignment);

        workOrder.setStatus(WorkOrderStatus.ASSIGNED);
        workOrderRepository.save(workOrder);

        activityLogService.logActivity(
                planner,
                "WORK_ORDER",
                workOrder.getId(),
                "ASSIGNED",
                "Work order assigned to " + technician.getFullName()
        );

        return mapToResponse(savedAssignment);
    }

    // Add the new method here
    private AssignmentResponse mapToResponse(Assignment assignment) {

        AssignmentResponse response = new AssignmentResponse();

        response.setId(assignment.getId());
        response.setWorkOrderId(assignment.getWorkOrder().getId());
        response.setWorkOrderNumber(
                assignment.getWorkOrder().getWorkOrderNumber()
        );
        response.setTechnicianId(assignment.getTechnician().getId());
        response.setTechnicianName(
                assignment.getTechnician().getFullName()
        );
        response.setStatus(assignment.getStatus().name());
        response.setRemarks(assignment.getRemarks());
        response.setAssignedAt(assignment.getAssignedAt());
        response.setRespondedAt(assignment.getRespondedAt());

        return response;
    }

    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public AssignmentResponse respondToAssignment(
        Long assignmentId,
        Long technicianId,
        TechnicianActionRequest request,
        boolean accepted) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (!assignment.getTechnician().getId().equals(technicianId)) {
            throw new RuntimeException("You are not assigned to this work order");
        }

        if (assignment.getStatus() != AssignmentStatus.ASSIGNED) {
            throw new RuntimeException("Assignment has already been responded to");
        }

        if (accepted) {
            assignment.setStatus(AssignmentStatus.ACCEPTED);
            assignment.getWorkOrder().setStatus(WorkOrderStatus.ACCEPTED);
        } else {
            assignment.setStatus(AssignmentStatus.REJECTED);
            assignment.getWorkOrder().setStatus(WorkOrderStatus.CREATED);
        }

        assignment.setRemarks(request.getRemarks());
        assignment.setRespondedAt(java.time.LocalDateTime.now());

        Assignment savedAssignment = assignmentRepository.save(assignment);
        workOrderRepository.save(assignment.getWorkOrder());

        activityLogService.logActivity(
                assignment.getTechnician(),
                "WORK_ORDER",
                assignment.getWorkOrder().getId(),
                accepted ? "ACCEPTED" : "REJECTED",
                accepted
                        ? "Technician accepted the work order"
                        : "Technician rejected the work order"
        );

        return mapToResponse(savedAssignment);
    }

    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public AssignmentResponse startWork(Long assignmentId, Long technicianId) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (!assignment.getTechnician().getId().equals(technicianId)) {
            throw new RuntimeException("You are not assigned to this work order");
        }

        if (assignment.getStatus() != AssignmentStatus.ACCEPTED) {
            throw new RuntimeException("You must accept the assignment first");
        }

        assignment.setStatus(AssignmentStatus.IN_PROGRESS);
        assignment.getWorkOrder().setStatus(WorkOrderStatus.IN_PROGRESS);

        Assignment savedAssignment = assignmentRepository.save(assignment);
        workOrderRepository.save(assignment.getWorkOrder());

        activityLogService.logActivity(
                assignment.getTechnician(),
                "WORK_ORDER",
                assignment.getWorkOrder().getId(),
                "STARTED",
                "Technician started the assigned work"
        );

        return mapToResponse(savedAssignment);
    }

    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public AssignmentResponse completeWork(
        Long assignmentId,
        Long technicianId,
        TechnicianActionRequest request) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (!assignment.getTechnician().getId().equals(technicianId)) {
            throw new RuntimeException("You are not assigned to this work order");
        }

        if (assignment.getStatus() != AssignmentStatus.IN_PROGRESS) {
            throw new RuntimeException("Work must be in progress before completion");
        }

        assignment.setStatus(AssignmentStatus.COMPLETED);
        assignment.setRemarks(request.getRemarks());
        assignment.getWorkOrder().setStatus(WorkOrderStatus.COMPLETED);

        Assignment savedAssignment = assignmentRepository.save(assignment);
        workOrderRepository.save(assignment.getWorkOrder());

        activityLogService.logActivity(
                assignment.getTechnician(),
                "WORK_ORDER",
                assignment.getWorkOrder().getId(),
                "COMPLETED",
                "Technician completed the assigned work: " + request.getRemarks()
        );

        return mapToResponse(savedAssignment);
    }
}