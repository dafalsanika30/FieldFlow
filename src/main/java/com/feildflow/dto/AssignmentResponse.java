package com.feildflow.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

import com.feildflow.entity.Assignment;

@Getter
@Setter
public class AssignmentResponse {

    private Long id;
    private Long workOrderId;
    private String workOrderNumber;
    private Long technicianId;
    private String technicianName;
    private String status;
    private String remarks;
    private LocalDateTime assignedAt;
    private LocalDateTime respondedAt;

    
}