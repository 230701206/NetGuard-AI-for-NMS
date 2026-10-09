package com.netguard.ai.controllers;

import com.netguard.ai.services.AlertCorrelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/correlation")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CorrelationController {

    private final AlertCorrelationService alertCorrelationService;

    @PostMapping("/run")
    public ResponseEntity<Map<String, Object>> runCorrelation() {
        List<AlertCorrelationService.CorrelationResult> results = alertCorrelationService.runCorrelation();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Correlation completed");
        response.put("results", results);
        response.put("correlatedGroups", results.size());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/results")
    public ResponseEntity<Map<String, Object>> getCorrelationResults() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Use POST /api/correlation/run to run correlation analysis");
        return ResponseEntity.ok(response);
    }
}
