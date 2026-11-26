package com.feildflow.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.feildflow.dto.CreateServiceRequestRequest;
import com.feildflow.dto.ServiceRequestResponse;
import com.feildflow.entity.ServiceRequest;
import com.feildflow.entity.ServiceRequestStatus;
import com.feildflow.entity.User;
import com.feildflow.entity.Zone;
import com.feildflow.repository.ServiceRequestRepository;
import com.feildflow.repository.UserRepository;
import com.feildflow.repository.ZoneRepository;

@Service 
public class ServiceRequestService {
    private final ServiceRequestRepository serviceRequestRepository;
    private final UserRepository userRepository;
    private final ZoneRepository zoneRepository;

    public ServiceRequestService(
            ServiceRequestRepository serviceRequestRepository,
            UserRepository userRepository,
            ZoneRepository zoneRepository) {

        this.serviceRequestRepository = serviceRequestRepository;
        this.userRepository = userRepository;
        this.zoneRepository = zoneRepository;
    }
    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public ServiceRequestResponse createRequest(
        CreateServiceRequestRequest request,
        Long requesterId) {

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("Requester not found"));

        Zone zone = zoneRepository.findById(request.getZoneId())
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        ServiceRequest serviceRequest = new ServiceRequest();

        serviceRequest.setTitle(request.getTitle());
        serviceRequest.setDescription(request.getDescription());
        serviceRequest.setLocation(request.getLocation());
        serviceRequest.setRequester(requester);
        serviceRequest.setZone(zone);
        serviceRequest.setStatus(ServiceRequestStatus.SUBMITTED);
        ServiceRequest savedRequest = serviceRequestRepository.save(serviceRequest);

        return mapToResponse(savedRequest);
    }

    private ServiceRequestResponse mapToResponse(ServiceRequest request) {

        ServiceRequestResponse response = new ServiceRequestResponse();

        response.setId(request.getId());
        response.setTitle(request.getTitle());
        response.setDescription(request.getDescription());
        response.setLocation(request.getLocation());
        response.setStatus(request.getStatus().name());
        response.setRequesterName(request.getRequester().getFullName());
        response.setZoneName(request.getZone().getZoneName());
        response.setCreatedAt(request.getCreatedAt());

        return response;
    }
    
    public List<ServiceRequestResponse> getAllRequests() {

        return serviceRequestRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ServiceRequestResponse getRequestById(Long id) {

        ServiceRequest request = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service request not found"));

        return mapToResponse(request);
    };

    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public ServiceRequestResponse approveRequest(Long id) {

        ServiceRequest request = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service request not found"));

        if (request.getStatus() != ServiceRequestStatus.SUBMITTED) {
            throw new RuntimeException("Only submitted requests can be approved");
        }

        request.setStatus(ServiceRequestStatus.APPROVED);

        ServiceRequest savedRequest = serviceRequestRepository.save(request);

        return mapToResponse(savedRequest);
    }

    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public ServiceRequestResponse rejectRequest(Long id) {

        ServiceRequest request = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service request not found"));

        if (request.getStatus() != ServiceRequestStatus.SUBMITTED) {
            throw new RuntimeException("Only submitted requests can be rejected");
        }

        request.setStatus(ServiceRequestStatus.REJECTED);

        ServiceRequest savedRequest = serviceRequestRepository.save(request);

        return mapToResponse(savedRequest);
    }
}
