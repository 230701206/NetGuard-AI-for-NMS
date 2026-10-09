package com.netguard.ai.config;

import com.netguard.ai.entities.*;
import com.netguard.ai.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final DeviceRepository deviceRepository;
    private final AlertRepository alertRepository;
    private final IncidentRepository incidentRepository;
    private final PredictionRepository predictionRepository;
    private final NetworkMetricRepository networkMetricRepository;

    @Override
    public void run(String... args) {
        if (deviceRepository.count() == 0) {
            log.info("Initializing sample data...");
            initializeDevices();
            initializeAlerts();
            initializeIncidents();
            initializePredictions();
            initializeNetworkMetrics();
            log.info("Sample data initialization completed");
        } else {
            log.info("Database already contains data, skipping initialization");
        }
    }

    private void initializeDevices() {
        List<Device> devices = Arrays.asList(
                createDevice("Router-01", "192.168.1.1", Device.DeviceType.ROUTER, "Data Center", Device.DeviceStatus.ONLINE, 45.0, 52.0, 0.2, 12.0),
                createDevice("Router-02", "192.168.1.2", Device.DeviceType.ROUTER, "Data Center", Device.DeviceStatus.ONLINE, 38.0, 48.0, 0.1, 8.0),
                createDevice("Switch-01", "192.168.1.10", Device.DeviceType.SWITCH, "Data Center", Device.DeviceStatus.ONLINE, 22.0, 35.0, 0.3, 5.0),
                createDevice("Switch-02", "192.168.1.11", Device.DeviceType.SWITCH, "Branch Office", Device.DeviceStatus.WARNING, 28.0, 42.0, 1.2, 15.0),
                createDevice("Server-01", "192.168.1.100", Device.DeviceType.SERVER, "Data Center", Device.DeviceStatus.ONLINE, 65.0, 72.0, 0.0, 2.0),
                createDevice("Server-02", "192.168.1.101", Device.DeviceType.SERVER, "Data Center", Device.DeviceStatus.ONLINE, 58.0, 68.0, 0.0, 3.0),
                createDevice("Firewall-01", "192.168.1.254", Device.DeviceType.FIREWALL, "Perimeter", Device.DeviceStatus.ONLINE, 32.0, 45.0, 0.1, 4.0)
        );

        deviceRepository.saveAll(devices);
        log.info("Created {} devices", devices.size());
    }

    private Device createDevice(String hostname, String ip, Device.DeviceType type, String location,
                                Device.DeviceStatus status, double cpu, double memory, double packetLoss, double latency) {
        Device device = new Device();
        device.setHostname(hostname);
        device.setIpAddress(ip);
        device.setDeviceType(type);
        device.setLocation(location);
        device.setStatus(status);
        device.setCpuUsage(cpu);
        device.setMemoryUsage(memory);
        device.setPacketLoss(packetLoss);
        device.setLatency(latency);
        device.setLastSeen(LocalDateTime.now());
        return device;
    }

    private void initializeAlerts() {
        List<Device> devices = deviceRepository.findAll();

        Alert alert1 = new Alert();
        alert1.setDevice(devices.get(0));
        alert1.setAlertType(Alert.AlertType.INTERFACE_DOWN);
        alert1.setSeverity(Alert.Severity.CRITICAL);
        alert1.setMessage("Interface Gi0/1 is down");
        alert1.setSource("SNMP");
        alert1.setTimestamp(LocalDateTime.now().minusHours(2));
        alert1.setStatus(Alert.AlertStatus.OPEN);
        alert1.setCorrelated(true);
        alert1.setCorrelationGroupId(1L);

        Alert alert2 = new Alert();
        alert2.setDevice(devices.get(2));
        alert2.setAlertType(Alert.AlertType.PACKET_LOSS);
        alert2.setSeverity(Alert.Severity.ERROR);
        alert2.setMessage("Packet loss exceeding 5%");
        alert2.setSource("SNMP");
        alert2.setTimestamp(LocalDateTime.now().minusHours(2).plusMinutes(2));
        alert2.setStatus(Alert.AlertStatus.OPEN);
        alert2.setCorrelated(true);
        alert2.setCorrelationGroupId(1L);

        Alert alert3 = new Alert();
        alert3.setDevice(devices.get(4));
        alert3.setAlertType(Alert.AlertType.CONNECTION_TIMEOUT);
        alert3.setSeverity(Alert.Severity.ERROR);
        alert3.setMessage("Connection timeout to database");
        alert3.setSource("Application");
        alert3.setTimestamp(LocalDateTime.now().minusHours(2).plusMinutes(5));
        alert3.setStatus(Alert.AlertStatus.OPEN);
        alert3.setCorrelated(true);
        alert3.setCorrelationGroupId(1L);

        Alert alert4 = new Alert();
        alert4.setDevice(devices.get(3));
        alert4.setAlertType(Alert.AlertType.HIGH_LATENCY);
        alert4.setSeverity(Alert.Severity.WARNING);
        alert4.setMessage("High latency detected");
        alert4.setSource("Network Monitor");
        alert4.setTimestamp(LocalDateTime.now().minusMinutes(30));
        alert4.setStatus(Alert.AlertStatus.OPEN);
        alert4.setCorrelated(false);

        Alert alert5 = new Alert();
        alert5.setDevice(devices.get(5));
        alert5.setAlertType(Alert.AlertType.CPU_HIGH);
        alert5.setSeverity(Alert.Severity.WARNING);
        alert5.setMessage("CPU usage at 85%");
        alert5.setSource("System Monitor");
        alert5.setTimestamp(LocalDateTime.now().minusMinutes(15));
        alert5.setStatus(Alert.AlertStatus.OPEN);
        alert5.setCorrelated(false);

        alertRepository.saveAll(Arrays.asList(alert1, alert2, alert3, alert4, alert5));
        log.info("Created {} alerts", 5);
    }

    private void initializeIncidents() {
        Incident incident1 = new Incident();
        incident1.setTitle("Router-01 Interface Failure");
        incident1.setSeverity(Alert.Severity.CRITICAL);
        incident1.setStatus(Incident.IncidentStatus.OPEN);
        incident1.setRootCause("Interface Gi0/1 failure");
        incident1.setRootCauseDevice("Router-01");
        incident1.setConfidenceScore(91.0);
        incident1.setAffectedDevices("Router-01, Switch-01, Server-01");
        incident1.setCreatedAt(LocalDateTime.now().minusHours(2));
        incident1.setAiRecommendation("Inspect Router-01 Gi0/1 interface and connected switch port. Check physical connectivity and configuration.");

        Incident incident2 = new Incident();
        incident2.setTitle("Switch-02 High Latency");
        incident2.setSeverity(Alert.Severity.WARNING);
        incident2.setStatus(Incident.IncidentStatus.INVESTIGATING);
        incident2.setRootCause("Network congestion");
        incident2.setRootCauseDevice("Switch-02");
        incident2.setConfidenceScore(78.0);
        incident2.setAffectedDevices("Switch-02");
        incident2.setCreatedAt(LocalDateTime.now().minusDays(1));
        incident2.setResolvedAt(LocalDateTime.now().minusHours(20));
        incident2.setAiRecommendation("Monitor traffic patterns and consider QoS configuration adjustments.");

        incidentRepository.saveAll(Arrays.asList(incident1, incident2));
        log.info("Created {} incidents", 2);
    }

    private void initializePredictions() {
        List<Device> devices = deviceRepository.findAll();

        Prediction pred1 = new Prediction();
        pred1.setDevice(devices.get(3));
        pred1.setFailureProbability(0.68);
        pred1.setRiskLevel(Prediction.RiskLevel.HIGH);
        pred1.setPredictedFailure("Potential performance degradation");
        pred1.setConfidence(0.82);
        pred1.setTimestamp(LocalDateTime.now().minusHours(1));
        pred1.setRecommendation("Monitor Switch-02 closely. High packet loss and latency indicators present.");

        Prediction pred2 = new Prediction();
        pred2.setDevice(devices.get(5));
        pred2.setFailureProbability(0.42);
        pred2.setRiskLevel(Prediction.RiskLevel.MEDIUM);
        pred2.setPredictedFailure("Possible resource exhaustion");
        pred2.setConfidence(0.75);
        pred2.setTimestamp(LocalDateTime.now().minusMinutes(30));
        pred2.setRecommendation("Continue monitoring Server-02 CPU and memory usage trends.");

        Prediction pred3 = new Prediction();
        pred3.setDevice(devices.get(0));
        pred3.setFailureProbability(0.15);
        pred3.setRiskLevel(Prediction.RiskLevel.LOW);
        pred3.setPredictedFailure("Stable operation expected");
        pred3.setConfidence(0.88);
        pred3.setTimestamp(LocalDateTime.now().minusMinutes(15));
        pred3.setRecommendation("Router-01 operating within normal parameters.");

        predictionRepository.saveAll(Arrays.asList(pred1, pred2, pred3));
        log.info("Created {} predictions", 3);
    }

    private void initializeNetworkMetrics() {
        List<Device> devices = deviceRepository.findAll();
        Random random = new Random();

        for (Device device : devices) {
            NetworkMetric metric = new NetworkMetric();
            metric.setDevice(device);
            metric.setCpuUsage(device.getCpuUsage() + (random.nextDouble() * 10 - 5));
            metric.setMemoryUsage(device.getMemoryUsage() + (random.nextDouble() * 10 - 5));
            metric.setLatency(device.getLatency() + (random.nextDouble() * 5 - 2.5));
            metric.setPacketLoss(Math.max(0, device.getPacketLoss() + (random.nextDouble() * 2 - 1)));
            metric.setInterfaceErrors(random.nextInt(50));
            metric.setTimestamp(LocalDateTime.now().minusMinutes(random.nextInt(60)));
            networkMetricRepository.save(metric);
        }

        log.info("Created network metrics for {} devices", devices.size());
    }
}
