package com.feildflow.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateWorkOrderRequest {

    private Long serviceRequestId;

    private String instructions;

    private String workOrderNumber;
}