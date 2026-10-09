package com.netguard.ai.controllers;

import com.netguard.ai.dtos.AlertDTO;
import com.netguard.ai.dtos.DashboardSummaryDTO;
import com.netguard.ai.dtos.IncidentDTO;
import com.netguard.ai.dtos.PredictionDTO;
import com.netguard.ai.services.AlertService;
import com.netguard.ai.services.DashboardService;
import com.netguard.ai.services.IncidentService;
import com.netguard.ai.services.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;
    private final AlertService alertService;
    private final IncidentService incidentService;
    private final PredictionService predictionService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<AlertDTO>> getRecentAlerts(@RequestParam(defaultValue = "10") int limit) {
        List<AlertDTO> alerts = alertService.getRecentAlerts(24);
        return ResponseEntity.ok(alerts.stream().limit(limit).toList());
    }

    @GetMapping("/incidents")
    public ResponseEntity<List<IncidentDTO>> getRecentIncidents(@RequestParam(defaultValue = "5") int limit) {
        List<IncidentDTO> incidents = incidentService.getOpenIncidents();
        return ResponseEntity.ok(incidents.stream().limit(limit).toList());
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        long totalAlerts = alertService.getAllAlerts().size();
        long criticalAlerts = alertService.getCriticalAlertCount();
        long openIncidents = incidentService.getOpenIncidentCount();
        long highRiskDevices = predictionService.getHighRiskDeviceCount();

        metrics.put("totalAlerts", totalAlerts);
        metrics.put("criticalAlerts", criticalAlerts);
        metrics.put("openIncidents", openIncidents);
        metrics.put("highRiskDevices", highRiskDevices);

        return ResponseEntity.ok(metrics);
    }
}
