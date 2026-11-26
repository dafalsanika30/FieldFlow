package com.feildflow.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.feildflow.dto.CreateServiceRequestRequest;
import com.feildflow.dto.ServiceRequestResponse;
import com.feildflow.service.ServiceRequestService;

@RestController 
@RequestMapping("/api/requests")
public class ServiceRequestController {
    private final ServiceRequestService serviceRequestService;

    public ServiceRequestController(ServiceRequestService serviceRequestService) {
        this.serviceRequestService = serviceRequestService;
    }

    @PostMapping
    public ResponseEntity<ServiceRequestResponse> createRequest(
            @RequestBody CreateServiceRequestRequest request,
            @RequestParam Long requesterId) {

        ServiceRequestResponse response =
                serviceRequestService.createRequest(request, requesterId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ServiceRequestResponse>> getAllRequest() {
        return ResponseEntity.ok(serviceRequestService.getAllRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequestResponse> getRequestById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                serviceRequestService.getRequestById(id)
        );
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ServiceRequestResponse> approveRequest(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                serviceRequestService.approveRequest(id)
        );
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ServiceRequestResponse> rejectRequest(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                serviceRequestService.rejectRequest(id)
        );
    }
    
}
