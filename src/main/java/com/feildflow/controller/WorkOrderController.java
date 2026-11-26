package com.feildflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.feildflow.dto.CreateWorkOrderRequest;
import com.feildflow.dto.WorkOrderResponse;
import com.feildflow.service.WorkOrderService;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    public WorkOrderController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    @PostMapping
    public ResponseEntity<WorkOrderResponse> createWorkOrder(
            @RequestBody CreateWorkOrderRequest request,
            @RequestParam Long plannerId) {

        return ResponseEntity.ok(
                workOrderService.createWorkOrder(request, plannerId)
        );
    }

    @PatchMapping("/{workOrderId}/verify")
    public ResponseEntity<WorkOrderResponse> verifyWorkOrder(
            @PathVariable Long workOrderId,
            @RequestParam Long supervisorId) {

        return ResponseEntity.ok(
                workOrderService.verifyWorkOrder(workOrderId, supervisorId)
        );
    }

    @PatchMapping("/{workOrderId}/close")
    public ResponseEntity<WorkOrderResponse> closeWorkOrder(
            @PathVariable Long workOrderId,
            @RequestParam Long supervisorId) {

        return ResponseEntity.ok(
                workOrderService.closeWorkOrder(workOrderId, supervisorId)
        );
    }


}