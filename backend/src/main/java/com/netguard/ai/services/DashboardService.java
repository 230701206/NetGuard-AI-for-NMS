package com.netguard.ai.services;

import com.netguard.ai.dtos.DashboardSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DeviceService deviceService;
    private final AlertService alertService;
    private final IncidentService incidentService;
    private final PredictionService predictionService;

    public DashboardSummaryDTO getSummary() {
        DashboardSummaryDTO summary = new DashboardSummaryDTO();

        long totalDevices = deviceService.getTotalDevices();
        long activeDevices = deviceService.getActiveDeviceCount();
        long criticalAlerts = alertService.getCriticalAlertCount();
        long openIncidents = incidentService.getOpenIncidentCount();
        long highRiskDevices = predictionService.getHighRiskDeviceCount();

        summary.setTotalDevices(totalDevices);
        summary.setActiveDevices(activeDevices);
        summary.setCriticalAlerts(criticalAlerts);
        summary.setOpenIncidents(openIncidents);
        summary.setHighRiskDevices(highRiskDevices);

        double networkHealth = calculateNetworkHealth(totalDevices, activeDevices, criticalAlerts, highRiskDevices);
        summary.setNetworkHealthPercentage(networkHealth);

        return summary;
    }

    private double calculateNetworkHealth(long totalDevices, long activeDevices, long criticalAlerts, long highRiskDevices) {
        if (totalDevices == 0) return 100.0;

        double deviceHealth = (double) activeDevices / totalDevices * 100.0;
        double alertPenalty = Math.min(20.0, criticalAlerts * 4.0);
        double riskPenalty = Math.min(10.0, highRiskDevices * 2.0);

        double health = deviceHealth - alertPenalty - riskPenalty;
        return Math.max(0.0, Math.min(100.0, health));
    }
}
