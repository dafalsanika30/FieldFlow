package com.feildflow.dto;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class CreateServiceRequestRequest {
    private String title;

    private String description;

    private String location;

    private Long zoneId;
}
