package com.feildflow.controller;

import com.feildflow.dto.CreateAssignmentRequest;
import com.feildflow.dto.AssignmentResponse;
import com.feildflow.service.AssignmentService;
import com.feildflow.dto.TechnicianActionRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<AssignmentResponse> assignTechnician(
            @RequestBody CreateAssignmentRequest request,
            @RequestParam Long plannerId) {

        return ResponseEntity.ok(
                assignmentService.assignTechnician(request, plannerId)
        );
    }

    @PatchMapping("/{assignmentId}/accept")
    public ResponseEntity<AssignmentResponse> acceptAssignment(
            @PathVariable Long assignmentId,
            @RequestParam Long technicianId,
            @RequestBody TechnicianActionRequest request) {

        return ResponseEntity.ok(
                assignmentService.respondToAssignment(
                        assignmentId, technicianId, request, true
                )
        );
    }

    @PatchMapping("/{assignmentId}/reject")
    public ResponseEntity<AssignmentResponse> rejectAssignment(
            @PathVariable Long assignmentId,
            @RequestParam Long technicianId,
            @RequestBody TechnicianActionRequest request) {

        return ResponseEntity.ok(
                assignmentService.respondToAssignment(
                        assignmentId, technicianId, request, false
                )
        );
    }

    @PatchMapping("/{assignmentId}/start")
    public ResponseEntity<AssignmentResponse> startWork(
            @PathVariable Long assignmentId,
            @RequestParam Long technicianId) {

        return ResponseEntity.ok(
                assignmentService.startWork(assignmentId, technicianId)
        );
    }

    @PatchMapping("/{assignmentId}/complete")
    public ResponseEntity<AssignmentResponse> completeWork(
            @PathVariable Long assignmentId,
            @RequestParam Long technicianId,
            @RequestBody TechnicianActionRequest request) {

        return ResponseEntity.ok(
                assignmentService.completeWork(
                        assignmentId, technicianId, request
                )
        );
    }
}