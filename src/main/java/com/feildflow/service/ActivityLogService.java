package com.feildflow.service;

import com.feildflow.entity.ActivityLog;
import com.feildflow.entity.Assignment;
import com.feildflow.entity.User;
import com.feildflow.repository.ActivityLogRepository;
import org.springframework.stereotype.Service;
import com.feildflow.dto.AssignmentResponse;

@Service
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    public ActivityLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    public void logActivity(
            User user,
            String entityType,
            Long entityId,
            String action,
            String details) {

        ActivityLog log = new ActivityLog();

        log.setUser(user);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setDetails(details);

        activityLogRepository.save(log);
    }

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
}