package com.netguard.ai.controllers;

import com.netguard.ai.dtos.PredictionDTO;
import com.netguard.ai.services.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/predictions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PredictionController {

    private final PredictionService predictionService;

    @GetMapping
    public ResponseEntity<List<PredictionDTO>> getAllPredictions() {
        return ResponseEntity.ok(predictionService.getAllPredictions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PredictionDTO> getPredictionById(@PathVariable Long id) {
        return ResponseEntity.ok(predictionService.getPredictionById(id));
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<PredictionDTO> getPredictionByDeviceId(@PathVariable Long deviceId) {
        PredictionDTO prediction = predictionService.getLatestPredictionByDeviceId(deviceId);
        if (prediction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(prediction);
    }

    @PostMapping("/run")
    public ResponseEntity<Map<String, Object>> runPredictions() {
        predictionService.runPredictionsForAllDevices();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Predictions completed for all devices");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/device/{deviceId}/run")
    public ResponseEntity<PredictionDTO> runPredictionForDevice(@PathVariable Long deviceId) {
        return ResponseEntity.ok(predictionService.runPrediction(deviceId));
    }
}
