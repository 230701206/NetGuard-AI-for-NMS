package com.netguard.ai.controllers;

import com.netguard.ai.entities.Alert;
import com.netguard.ai.repositories.AlertRepository;
import com.netguard.ai.services.RootCauseAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/root-cause")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RootCauseController {

    private final RootCauseAnalysisService rootCauseAnalysisService;
    private final AlertRepository alertRepository;

    @GetMapping("/incident/{incidentId}")
    public ResponseEntity<Map<String, Object>> analyzeIncidentRootCause(@PathVariable Long incidentId) {
        List<Alert> incidentAlerts = alertRepository.findAll().stream()
                .filter(a -> a.getCorrelationGroupId() != null && a.getCorrelationGroupId().equals(incidentId))
                .collect(Collectors.toList());

        if (incidentAlerts.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "No alerts found for this incident");
            return ResponseEntity.ok(response);
        }

        RootCauseAnalysisService.RootCauseAnalysisResult result = rootCauseAnalysisService.analyzeRootCause(incidentAlerts);

        Map<String, Object> response = new HashMap<>();
        response.put("rootCauseDevice", result.getRootCauseDevice());
        response.put("rootCauseType", result.getRootCauseType());
        response.put("confidenceScore", result.getConfidenceScore());
        response.put("explanation", result.getExplanation());
        response.put("analysisTimestamp", result.getAnalysisTimestamp());

        return ResponseEntity.ok(response);
    }
}
