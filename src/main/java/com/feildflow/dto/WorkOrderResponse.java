package com.feildflow.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class WorkOrderResponse {

    private Long id;
    private String workOrderNumber;
    private String title;
    private String instructions;
    private String status;
    private Long serviceRequestId;
    private LocalDateTime createdAt;
}