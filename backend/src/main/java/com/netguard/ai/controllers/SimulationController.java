package com.netguard.ai.controllers;

import com.netguard.ai.dtos.AlertDTO;
import com.netguard.ai.dtos.CreateAlertDTO;
import com.netguard.ai.dtos.IncidentDTO;
import com.netguard.ai.entities.Alert;
import com.netguard.ai.entities.Device;
import com.netguard.ai.repositories.AlertRepository;
import com.netguard.ai.repositories.DeviceRepository;
import com.netguard.ai.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/simulation")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SimulationController {

    private final AlertService alertService;
    private final DeviceService deviceService;
    private final AlertCorrelationService alertCorrelationService;
    private final RootCauseAnalysisService rootCauseAnalysisService;
    private final IncidentService incidentService;
    private final PredictionService predictionService;
    private final DeviceRepository deviceRepository;
    private final AlertRepository alertRepository;

    @PostMapping("/router-failure")
    public ResponseEntity<Map<String, Object>> simulateRouterFailure() {
        List<com.netguard.ai.dtos.DeviceDTO> devices = deviceService.getAllDevices().stream()
                .filter(d -> d.getHostname().contains("Router"))
                .toList();

        if (devices.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No router devices found"));
        }

        Device router = getDevice(devices.get(0));
        List<Alert> alerts = new ArrayList<>();

        alerts.add(createAlert(router, Alert.AlertType.INTERFACE_DOWN, Alert.Severity.CRITICAL,
                "Interface Gi0/1 is down on Router"));

        List<com.netguard.ai.dtos.DeviceDTO> switches = deviceService.getAllDevices().stream()
                .filter(d -> d.getHostname().contains("Switch"))
                .limit(2)
                .toList();

        for (com.netguard.ai.dtos.DeviceDTO swDto : switches) {
            Device sw = getDevice(swDto);
            alerts.add(createAlert(sw, Alert.AlertType.PACKET_LOSS, Alert.Severity.ERROR,
                    "High packet loss detected on Switch"));
            alerts.add(createAlert(sw, Alert.AlertType.HIGH_LATENCY, Alert.Severity.WARNING,
                    "High latency on Switch"));
        }

        List<com.netguard.ai.dtos.DeviceDTO> servers = deviceService.getAllDevices().stream()
                .filter(d -> d.getHostname().contains("Server"))
                .limit(2)
                .toList();

        for (com.netguard.ai.dtos.DeviceDTO serverDto : servers) {
            Device server = getDevice(serverDto);
            alerts.add(createAlert(server, Alert.AlertType.CONNECTION_TIMEOUT, Alert.Severity.ERROR,
                    "Connection timeout to Server"));
            alerts.add(createAlert(server, Alert.AlertType.SERVICE_DOWN, Alert.Severity.CRITICAL,
                    "Service down on Server"));
        }

        List<AlertCorrelationService.CorrelationResult> correlationResults = alertCorrelationService.runCorrelation();

        RootCauseAnalysisService.RootCauseAnalysisResult rcaResult = rootCauseAnalysisService.analyzeRootCause(alerts);

        IncidentDTO incident = incidentService.createIncidentFromAlerts(
                alerts,
                rcaResult.getRootCauseType().toString(),
                rcaResult.getRootCauseDevice(),
                rcaResult.getConfidenceScore()
        );

        predictionService.runPredictionsForAllDevices();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Router failure simulation completed");
        response.put("alertsGenerated", alerts.size());
        response.put("correlationGroups", correlationResults.size());
        response.put("incidentCreated", incident);
        response.put("rootCause", Map.of(
                "device", rcaResult.getRootCauseDevice(),
                "type", rcaResult.getRootCauseType(),
                "confidence", rcaResult.getConfidenceScore(),
                "explanation", rcaResult.getExplanation()
        ));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/packet-loss")
    public ResponseEntity<Map<String, Object>> simulatePacketLoss() {
        List<com.netguard.ai.dtos.DeviceDTO> switches = deviceService.getAllDevices().stream()
                .filter(d -> d.getHostname().contains("Switch"))
                .toList();

        if (switches.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No switch devices found"));
        }

        Device sw = getDevice(switches.get(0));
        List<Alert> alerts = new ArrayList<>();

        alerts.add(createAlert(sw, Alert.AlertType.PACKET_LOSS, Alert.Severity.ERROR,
                "Packet loss exceeding threshold on Switch"));

        List<com.netguard.ai.dtos.DeviceDTO> servers = deviceService.getAllDevices().stream()
                .filter(d -> d.getHostname().contains("Server"))
                .limit(2)
                .toList();

        for (com.netguard.ai.dtos.DeviceDTO serverDto : servers) {
            Device server = getDevice(serverDto);
            alerts.add(createAlert(server, Alert.AlertType.HIGH_LATENCY, Alert.Severity.WARNING,
                    "High latency due to packet loss"));
        }

        alertCorrelationService.runCorrelation();

        predictionService.runPredictionsForAllDevices();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Packet loss simulation completed");
        response.put("alertsGenerated", alerts.size());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/high-cpu")
    public ResponseEntity<Map<String, Object>> simulateHighCPU() {
        List<com.netguard.ai.dtos.DeviceDTO> servers = deviceService.getAllDevices().stream()
                .filter(d -> d.getHostname().contains("Server"))
                .toList();

        if (servers.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No server devices found"));
        }

        Device server = getDevice(servers.get(0));
        server.setCpuUsage(92.0);
        server.setMemoryUsage(78.0);
        deviceService.updateDevice(server.getId(), server);

        Alert alert = createAlert(server, Alert.AlertType.CPU_HIGH, Alert.Severity.WARNING,
                "CPU usage exceeding threshold");

        predictionService.runPrediction(server.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "High CPU simulation completed");
        response.put("device", server.getHostname());
        response.put("cpuUsage", server.getCpuUsage());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/random-alerts")
    public ResponseEntity<Map<String, Object>> generateRandomAlerts(@RequestParam(defaultValue = "5") int count) {
        List<com.netguard.ai.dtos.DeviceDTO> devices = deviceService.getAllDevices();
        List<Alert> alerts = new ArrayList<>();

        Alert.AlertType[] alertTypes = Alert.AlertType.values();
        Alert.Severity[] severities = {Alert.Severity.INFO, Alert.Severity.WARNING, Alert.Severity.ERROR};

        for (int i = 0; i < count; i++) {
            Device device = getDevice(devices.get(ThreadLocalRandom.current().nextInt(devices.size())));
            Alert.AlertType alertType = alertTypes[ThreadLocalRandom.current().nextInt(alertTypes.length)];
            Alert.Severity severity = severities[ThreadLocalRandom.current().nextInt(severities.length)];

            Alert alert = createAlert(device, alertType, severity,
                    "Simulated " + alertType + " alert");
            alerts.add(alert);
        }

        alertCorrelationService.runCorrelation();
        predictionService.runPredictionsForAllDevices();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Random alerts generated");
        response.put("alertsGenerated", alerts.size());

        return ResponseEntity.ok(response);
    }

    private Alert createAlert(Device device, Alert.AlertType alertType, Alert.Severity severity, String message) {
        CreateAlertDTO dto = new CreateAlertDTO();
        dto.setDeviceId(device.getId());
        dto.setAlertType(alertType.toString());
        dto.setSeverity(severity.toString());
        dto.setMessage(message);
        dto.setSource("SIMULATION");

        AlertDTO created = alertService.createAlert(dto);
        return alertRepository.findById(created.getId())
                .orElseThrow(() -> new IllegalStateException("Created alert could not be reloaded"));
    }

    private Device getDevice(com.netguard.ai.dtos.DeviceDTO dto) {
        return deviceRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalStateException("Device could not be reloaded: " + dto.getId()));
    }
}
