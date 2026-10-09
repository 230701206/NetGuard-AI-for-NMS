package com.netguard.ai.services;

import com.netguard.ai.dtos.PredictionDTO;
import com.netguard.ai.entities.Device;
import com.netguard.ai.entities.NetworkMetric;
import com.netguard.ai.entities.Prediction;
import com.netguard.ai.exception.ResourceNotFoundException;
import com.netguard.ai.repositories.AlertRepository;
import com.netguard.ai.repositories.DeviceRepository;
import com.netguard.ai.repositories.NetworkMetricRepository;
import com.netguard.ai.repositories.PredictionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionService {

    private final PredictionRepository predictionRepository;
    private final DeviceRepository deviceRepository;
    private final AlertRepository alertRepository;
    private final NetworkMetricRepository networkMetricRepository;

    @Value("${ml.service.url:http://localhost:5000}")
    private String mlServiceUrl;

    @Value("${ml.service.enabled:true}")
    private boolean mlServiceEnabled;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<PredictionDTO> getAllPredictions() {
        return predictionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PredictionDTO getPredictionById(Long id) {
        Prediction prediction = predictionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prediction", id));
        return convertToDTO(prediction);
    }

    public List<PredictionDTO> getPredictionsByDeviceId(Long deviceId) {
        return predictionRepository.findByDeviceId(deviceId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PredictionDTO getLatestPredictionByDeviceId(Long deviceId) {
        Optional<Prediction> prediction = predictionRepository.findTopByDeviceIdOrderByTimestampDesc(deviceId);
        return prediction.map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public PredictionDTO runPrediction(Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device", deviceId));

        Prediction prediction;

        if (mlServiceEnabled) {
            prediction = callMLService(device);
        } else {
            prediction = calculateFallbackPrediction(device);
        }

        return convertToDTO(predictionRepository.save(prediction));
    }

    @Transactional
    public void runPredictionsForAllDevices() {
        List<Device> devices = deviceRepository.findAll();
        log.info("Running predictions for {} devices", devices.size());

        for (Device device : devices) {
            try {
                runPrediction(device.getId());
            } catch (Exception e) {
                log.error("Failed to run prediction for device {}: {}", device.getHostname(), e.getMessage());
            }
        }
    }

    private Prediction callMLService(Device device) {
        try {
            Map<String, Object> features = extractFeatures(device);

            Map<String, Object> response = restTemplate.postForObject(
                    mlServiceUrl + "/predict",
                    features,
                    Map.class
            );

            if (response != null) {
                Prediction prediction = new Prediction();
                prediction.setDevice(device);
                prediction.setFailureProbability(((Number) response.getOrDefault("failure_probability", 0.0)).doubleValue());
                prediction.setRiskLevel(Prediction.RiskLevel.valueOf((String) response.getOrDefault("risk_level", "MEDIUM")));
                prediction.setPredictedFailure((String) response.getOrDefault("predicted_failure", "Unknown"));
                prediction.setConfidence(((Number) response.getOrDefault("confidence", 0.0)).doubleValue());
                prediction.setTimestamp(LocalDateTime.now());
                prediction.setRecommendation((String) response.getOrDefault("recommendation", "Monitor device closely"));

                return prediction;
            }
        } catch (Exception e) {
            log.warn("ML service call failed, using fallback prediction: {}", e.getMessage());
        }

        return calculateFallbackPrediction(device);
    }

    private Prediction calculateFallbackPrediction(Device device) {
        Prediction prediction = new Prediction();
        prediction.setDevice(device);
        prediction.setTimestamp(LocalDateTime.now());

        double riskScore = 0.0;

        if (device.getCpuUsage() != null && device.getCpuUsage() > 80) {
            riskScore += 25;
        }
        if (device.getMemoryUsage() != null && device.getMemoryUsage() > 80) {
            riskScore += 20;
        }
        if (device.getPacketLoss() != null && device.getPacketLoss() > 5) {
            riskScore += 30;
        }
        if (device.getLatency() != null && device.getLatency() > 100) {
            riskScore += 15;
        }

        long recentCriticalAlerts = alertRepository.findByDeviceId(device.getId()).stream()
                .filter(a -> a.getSeverity() == com.netguard.ai.entities.Alert.Severity.CRITICAL)
                .filter(a -> a.getTimestamp().isAfter(LocalDateTime.now().minusHours(24)))
                .count();

        riskScore += recentCriticalAlerts * 10;

        riskScore = Math.min(100.0, riskScore);

        prediction.setFailureProbability(riskScore / 100.0);

        if (riskScore >= 75) {
            prediction.setRiskLevel(Prediction.RiskLevel.CRITICAL);
            prediction.setPredictedFailure("High probability of device failure");
            prediction.setRecommendation("Immediate investigation required. Check system resources and logs.");
        } else if (riskScore >= 50) {
            prediction.setRiskLevel(Prediction.RiskLevel.HIGH);
            prediction.setPredictedFailure("Potential performance degradation");
            prediction.setRecommendation("Monitor closely. Consider preventive maintenance.");
        } else if (riskScore >= 25) {
            prediction.setRiskLevel(Prediction.RiskLevel.MEDIUM);
            prediction.setPredictedFailure("Possible minor issues");
            prediction.setRecommendation("Regular monitoring recommended.");
        } else {
            prediction.setRiskLevel(Prediction.RiskLevel.LOW);
            prediction.setPredictedFailure("Stable operation expected");
            prediction.setRecommendation("Continue normal monitoring.");
        }

        prediction.setConfidence(0.75);

        return prediction;
    }

    private Map<String, Object> extractFeatures(Device device) {
        Map<String, Object> features = new HashMap<>();
        features.put("cpu_usage", device.getCpuUsage() != null ? device.getCpuUsage() : 0.0);
        features.put("memory_usage", device.getMemoryUsage() != null ? device.getMemoryUsage() : 0.0);
        features.put("packet_loss", device.getPacketLoss() != null ? device.getPacketLoss() : 0.0);
        features.put("latency", device.getLatency() != null ? device.getLatency() : 0.0);

        long recentCriticalAlerts = alertRepository.findByDeviceId(device.getId()).stream()
                .filter(a -> a.getSeverity() == com.netguard.ai.entities.Alert.Severity.CRITICAL)
                .filter(a -> a.getTimestamp().isAfter(LocalDateTime.now().minusHours(24)))
                .count();
        features.put("recent_critical_alerts", recentCriticalAlerts);

        Optional<NetworkMetric> latestMetric = networkMetricRepository.findTopByDeviceIdOrderByTimestampDesc(device.getId());
        latestMetric.ifPresent(metric -> {
            features.put("interface_errors", metric.getInterfaceErrors() != null ? metric.getInterfaceErrors() : 0);
        });

        return features;
    }

    public long getHighRiskDeviceCount() {
        return predictionRepository.countByRiskLevel(Prediction.RiskLevel.HIGH) +
               predictionRepository.countByRiskLevel(Prediction.RiskLevel.CRITICAL);
    }

    private PredictionDTO convertToDTO(Prediction prediction) {
        PredictionDTO dto = new PredictionDTO();
        dto.setId(prediction.getId());
        dto.setDeviceId(prediction.getDevice().getId());
        dto.setDeviceHostname(prediction.getDevice().getHostname());
        dto.setFailureProbability(prediction.getFailureProbability());
        dto.setRiskLevel(prediction.getRiskLevel());
        dto.setPredictedFailure(prediction.getPredictedFailure());
        dto.setConfidence(prediction.getConfidence());
        dto.setTimestamp(prediction.getTimestamp());
        dto.setRecommendation(prediction.getRecommendation());
        return dto;
    }
}
