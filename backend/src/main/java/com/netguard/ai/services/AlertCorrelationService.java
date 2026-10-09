package com.netguard.ai.services;

import com.netguard.ai.dtos.AlertDTO;
import com.netguard.ai.entities.Alert;
import com.netguard.ai.entities.Device;
import com.netguard.ai.repositories.AlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertCorrelationService {

    private final AlertRepository alertRepository;
    private final IncidentService incidentService;

    @Value("${correlation.time.window.minutes:5}")
    private int timeWindowMinutes;

    @Value("${correlation.min.score:60}")
    private int minCorrelationScore;

    @Transactional
    public List<CorrelationResult> runCorrelation() {
        log.info("Starting alert correlation analysis");

        List<Alert> uncorrelatedAlerts = alertRepository.findUncorrelatedAlerts();
        log.info("Found {} uncorrelated alerts", uncorrelatedAlerts.size());

        List<CorrelationResult> results = new ArrayList<>();

        if (uncorrelatedAlerts.isEmpty()) {
            return results;
        }

        List<Alert> criticalAlerts = uncorrelatedAlerts.stream()
                .filter(a -> a.getSeverity() == Alert.Severity.CRITICAL || a.getSeverity() == Alert.Severity.ERROR)
                .sorted(Comparator.comparing(Alert::getTimestamp))
                .collect(Collectors.toList());

        for (Alert baseAlert : criticalAlerts) {
            if (baseAlert.getCorrelated()) {
                continue;
            }

            List<Alert> relatedAlerts = findRelatedAlerts(baseAlert, uncorrelatedAlerts);

            if (relatedAlerts.size() >= 2) {
                double correlationScore = calculateCorrelationScore(baseAlert, relatedAlerts);

                if (correlationScore >= minCorrelationScore) {
                    CorrelationResult result = new CorrelationResult();
                    result.setBaseAlertId(baseAlert.getId());
                    result.setRelatedAlertIds(relatedAlerts.stream()
                            .map(Alert::getId)
                            .collect(Collectors.toList()));
                    result.setCorrelationScore(correlationScore);
                    result.setCorrelationReason(generateCorrelationReason(baseAlert, relatedAlerts));

                    Long groupId = System.currentTimeMillis();
                    baseAlert.setCorrelated(true);
                    baseAlert.setCorrelationGroupId(groupId);

                    for (Alert related : relatedAlerts) {
                        related.setCorrelated(true);
                        related.setCorrelationGroupId(groupId);
                    }

                    alertRepository.saveAll(relatedAlerts);
                    alertRepository.save(baseAlert);

                    results.add(result);

                    log.info("Correlated {} alerts with score {}", relatedAlerts.size() + 1, correlationScore);
                }
            }
        }

        log.info("Correlation completed. Found {} groups", results.size());
        return results;
    }

    private List<Alert> findRelatedAlerts(Alert baseAlert, List<Alert> allAlerts) {
        LocalDateTime windowStart = baseAlert.getTimestamp().minusMinutes(timeWindowMinutes);
        LocalDateTime windowEnd = baseAlert.getTimestamp().plusMinutes(timeWindowMinutes);

        return allAlerts.stream()
                .filter(a -> !a.getId().equals(baseAlert.getId()) && !a.getCorrelated())
                .filter(a -> a.getTimestamp().isAfter(windowStart) && a.getTimestamp().isBefore(windowEnd))
                .filter(a -> isDeviceRelated(baseAlert.getDevice(), a.getDevice()))
                .filter(a -> isAlertTypeRelated(baseAlert.getAlertType(), a.getAlertType()))
                .collect(Collectors.toList());
    }

    private boolean isDeviceRelated(Device device1, Device device2) {
        if (device1.getId().equals(device2.getId())) {
            return true;
        }

        String host1 = device1.getHostname().toLowerCase();
        String host2 = device2.getHostname().toLowerCase();

        if (host1.contains("router") && host2.contains("switch")) return true;
        if (host1.contains("switch") && host2.contains("server")) return true;
        if (host1.contains("router") && host2.contains("server")) return true;

        return false;
    }

    private boolean isAlertTypeRelated(Alert.AlertType type1, Alert.AlertType type2) {
        Map<Alert.AlertType, List<Alert.AlertType>> relatedTypes = new HashMap<>();
        relatedTypes.put(Alert.AlertType.INTERFACE_DOWN, Arrays.asList(
                Alert.AlertType.PACKET_LOSS, Alert.AlertType.HIGH_LATENCY, Alert.AlertType.CONNECTION_TIMEOUT
        ));
        relatedTypes.put(Alert.AlertType.PACKET_LOSS, Arrays.asList(
                Alert.AlertType.HIGH_LATENCY, Alert.AlertType.CONNECTION_TIMEOUT, Alert.AlertType.SERVICE_DOWN
        ));
        relatedTypes.put(Alert.AlertType.HIGH_LATENCY, Arrays.asList(
                Alert.AlertType.CONNECTION_TIMEOUT, Alert.AlertType.SERVICE_DOWN
        ));

        List<Alert.AlertType> related = relatedTypes.getOrDefault(type1, Collections.emptyList());
        return related.contains(type2);
    }

    private double calculateCorrelationScore(Alert baseAlert, List<Alert> relatedAlerts) {
        double score = 50.0;

        double timeScore = 30.0;
        double deviceScore = 15.0;
        double severityScore = 5.0;

        for (Alert alert : relatedAlerts) {
            long minutesDiff = Math.abs(ChronoUnit.MINUTES.between(baseAlert.getTimestamp(), alert.getTimestamp()));
            if (minutesDiff <= 1) {
                timeScore += 5.0;
            } else if (minutesDiff <= 3) {
                timeScore += 3.0;
            }

            if (isDeviceRelated(baseAlert.getDevice(), alert.getDevice())) {
                deviceScore += 5.0;
            }

            if (alert.getSeverity() == Alert.Severity.CRITICAL || alert.getSeverity() == Alert.Severity.ERROR) {
                severityScore += 2.0;
            }
        }

        score = Math.min(100.0, score + timeScore + deviceScore + severityScore);
        return Math.round(score * 100.0) / 100.0;
    }

    private String generateCorrelationReason(Alert baseAlert, List<Alert> relatedAlerts) {
        return String.format(
                "Alerts correlated based on temporal proximity (%d min window), device topology relationship, and alert type patterns",
                timeWindowMinutes
        );
    }

    public static class CorrelationResult {
        private Long baseAlertId;
        private List<Long> relatedAlertIds;
        private double correlationScore;
        private String correlationReason;

        public Long getBaseAlertId() { return baseAlertId; }
        public void setBaseAlertId(Long baseAlertId) { this.baseAlertId = baseAlertId; }
        public List<Long> getRelatedAlertIds() { return relatedAlertIds; }
        public void setRelatedAlertIds(List<Long> relatedAlertIds) { this.relatedAlertIds = relatedAlertIds; }
        public double getCorrelationScore() { return correlationScore; }
        public void setCorrelationScore(double correlationScore) { this.correlationScore = correlationScore; }
        public String getCorrelationReason() { return correlationReason; }
        public void setCorrelationReason(String correlationReason) { this.correlationReason = correlationReason; }
    }
}
