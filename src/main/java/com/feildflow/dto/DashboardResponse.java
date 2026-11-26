package com.feildflow.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardResponse implements java.io.Serializable {

    private long totalRequests;
    private long totalWorkOrders;
    private long totalAssignments;

}