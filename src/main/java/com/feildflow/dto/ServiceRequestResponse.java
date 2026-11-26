package com.feildflow.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceRequestResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private String status;
    private String requesterName;
    private String zoneName;
    private LocalDateTime createdAt;

    
}