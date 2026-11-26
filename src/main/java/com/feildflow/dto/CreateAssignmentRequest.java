package com.feildflow.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAssignmentRequest {
    private Long workOrderId;
    private Long technicianId;
    private String remarks;
}