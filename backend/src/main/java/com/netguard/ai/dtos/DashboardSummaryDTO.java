package com.netguard.ai.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {
    private Long totalDevices;
    private Long activeDevices;
    private Long criticalAlerts;
    private Long openIncidents;
    private Long highRiskDevices;
    private Double networkHealthPercentage;
}
