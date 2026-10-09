package com.netguard.ai.services;

import com.netguard.ai.entities.Alert;
import com.netguard.ai.entities.Device;
import com.netguard.ai.repositories.AlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RootCauseAnalysisService {

    private final AlertRepository alertRepository;
    private final IncidentService incidentService;

    public RootCauseAnalysisResult analyzeRootCause(List<Alert> alerts) {
        if (alerts == null || alerts.isEmpty()) {
            return createEmptyResult();
        }

        log.info("Analyzing root cause for {} alerts", alerts.size());

        List<Alert> sortedAlerts = alerts.stream()
                .sorted(Comparator.comparing(Alert::getTimestamp))
                .collect(Collectors.toList());

        Alert earliestCriticalAlert = findEarliestCriticalAlert(sortedAlerts);
        Alert earliestAlert = sortedAlerts.get(0);

        Map<Alert, Double> scores = new HashMap<>();

        for (Alert alert : sortedAlerts) {
            double score = calculateRootCauseScore(alert, sortedAlerts);
            scores.put(alert, score);
        }

        Alert mostLikelyRootCause = scores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(earliestAlert);

        double confidenceScore = scores.get(mostLikelyRootCause);

        String explanation = generateExplanation(mostLikelyRootCause, sortedAlerts, confidenceScore);

        RootCauseAnalysisResult result = new RootCauseAnalysisResult();
        result.setRootCauseAlert(mostLikelyRootCause);
        result.setRootCauseDevice(mostLikelyRootCause.getDevice().getHostname());
        result.setRootCauseType(mostLikelyRootCause.getAlertType());
        result.setConfidenceScore(Math.round(confidenceScore * 100.0) / 100.0);
        result.setExplanation(explanation);
        result.setAnalysisTimestamp(java.time.LocalDateTime.now());

        log.info("Root cause identified: {} on {} with confidence {}",
                mostLikelyRootCause.getAlertType(),
                mostLikelyRootCause.getDevice().getHostname(),
                confidenceScore);

        return result;
    }

    private Alert findEarliestCriticalAlert(List<Alert> alerts) {
        return alerts.stream()
                .filter(a -> a.getSeverity() == Alert.Severity.CRITICAL || a.getSeverity() == Alert.Severity.ERROR)
                .min(Comparator.comparing(Alert::getTimestamp))
                .orElse(alerts.get(0));
    }

    private double calculateRootCauseScore(Alert alert, List<Alert> allAlerts) {
        double score = 0.0;

        double temporalScore = calculateTemporalScore(alert, allAlerts);
        double severityScore = calculateSeverityScore(alert);
        double deviceTopologyScore = calculateDeviceTopologyScore(alert, allAlerts);
        double alertTypeScore = calculateAlertTypeScore(alert);

        score = temporalScore * 0.4 + severityScore * 0.25 + deviceTopologyScore * 0.25 + alertTypeScore * 0.1;

        return Math.min(100.0, score);
    }

    private double calculateTemporalScore(Alert alert, List<Alert> allAlerts) {
        if (allAlerts.size() == 1) return 100.0;

        long minutesToFirst = ChronoUnit.MINUTES.between(alert.getTimestamp(), allAlerts.get(0).getTimestamp());
        long minutesToLast = ChronoUnit.MINUTES.between(alert.getTimestamp(), allAlerts.get(allAlerts.size() - 1).getTimestamp());

        if (minutesToFirst == 0) return 100.0;

        long totalSpan = minutesToLast - minutesToFirst;
        if (totalSpan == 0) return 100.0;

        double position = (double) minutesToFirst / totalSpan;
        return Math.max(0.0, 100.0 - (position * 50.0));
    }

    private double calculateSeverityScore(Alert alert) {
        return switch (alert.getSeverity()) {
            case CRITICAL -> 100.0;
            case ERROR -> 85.0;
            case WARNING -> 60.0;
            case INFO -> 30.0;
        };
    }

    private double calculateDeviceTopologyScore(Alert alert, List<Alert> allAlerts) {
        Device alertDevice = alert.getDevice();
        String hostname = alertDevice.getHostname().toLowerCase();

        if (hostname.contains("router")) {
            long downstreamAlerts = allAlerts.stream()
                    .filter(a -> !a.getDevice().getId().equals(alertDevice.getId()))
                    .filter(a -> a.getTimestamp().isAfter(alert.getTimestamp()))
                    .count();

            if (downstreamAlerts > 0) {
                return 100.0;
            }
        }

        if (hostname.contains("switch")) {
            long downstreamAlerts = allAlerts.stream()
                    .filter(a -> a.getDevice().getHostname().toLowerCase().contains("server"))
                    .filter(a -> a.getTimestamp().isAfter(alert.getTimestamp()))
                    .count();

            if (downstreamAlerts > 0) {
                return 85.0;
            }
        }

        return 50.0;
    }

    private double calculateAlertTypeScore(Alert alert) {
        return switch (alert.getAlertType()) {
            case INTERFACE_DOWN -> 100.0;
            case ROUTING_ERROR -> 95.0;
            case PACKET_LOSS -> 80.0;
            case HIGH_LATENCY -> 70.0;
            case CONNECTION_TIMEOUT -> 60.0;
            case SERVICE_DOWN -> 50.0;
            case CPU_HIGH, MEMORY_HIGH -> 40.0;
            default -> 30.0;
        };
    }

    private String generateExplanation(Alert rootCause, List<Alert> allAlerts, double confidence) {
        StringBuilder explanation = new StringBuilder();

        explanation.append(String.format("%s on %s is identified as the most probable root cause with %.1f%% confidence.\n\n",
                rootCause.getAlertType(),
                rootCause.getDevice().getHostname(),
                confidence));

        explanation.append("Reasoning:\n");

        if (rootCause.getSeverity() == Alert.Severity.CRITICAL) {
            explanation.append("- Critical severity alert detected\n");
        }

        if (rootCause.equals(allAlerts.get(0))) {
            explanation.append("- Earliest event in the incident timeline\n");
        }

        String hostname = rootCause.getDevice().getHostname().toLowerCase();
        if (hostname.contains("router")) {
            explanation.append("- Router failure impacts downstream network devices\n");
        } else if (hostname.contains("switch")) {
            explanation.append("- Switch failure affects connected servers\n");
        }

        long downstreamCount = allAlerts.stream()
                .filter(a -> a.getTimestamp().isAfter(rootCause.getTimestamp()))
                .count();

        if (downstreamCount > 0) {
            explanation.append(String.format("- %d downstream alerts followed this event\n", downstreamCount));
        }

        if (rootCause.getAlertType() == Alert.AlertType.INTERFACE_DOWN) {
            explanation.append("- Interface failure typically causes cascading connectivity issues\n");
        }

        return explanation.toString();
    }

    private RootCauseAnalysisResult createEmptyResult() {
        RootCauseAnalysisResult result = new RootCauseAnalysisResult();
        result.setConfidenceScore(0.0);
        result.setExplanation("No alerts provided for analysis");
        result.setAnalysisTimestamp(java.time.LocalDateTime.now());
        return result;
    }

    public static class RootCauseAnalysisResult {
        private Alert rootCauseAlert;
        private String rootCauseDevice;
        private Alert.AlertType rootCauseType;
        private double confidenceScore;
        private String explanation;
        private java.time.LocalDateTime analysisTimestamp;

        public Alert getRootCauseAlert() { return rootCauseAlert; }
        public void setRootCauseAlert(Alert rootCauseAlert) { this.rootCauseAlert = rootCauseAlert; }
        public String getRootCauseDevice() { return rootCauseDevice; }
        public void setRootCauseDevice(String rootCauseDevice) { this.rootCauseDevice = rootCauseDevice; }
        public Alert.AlertType getRootCauseType() { return rootCauseType; }
        public void setRootCauseType(Alert.AlertType rootCauseType) { this.rootCauseType = rootCauseType; }
        public double getConfidenceScore() { return confidenceScore; }
        public void setConfidenceScore(double confidenceScore) { this.confidenceScore = confidenceScore; }
        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }
        public java.time.LocalDateTime getAnalysisTimestamp() { return analysisTimestamp; }
        public void setAnalysisTimestamp(java.time.LocalDateTime analysisTimestamp) { this.analysisTimestamp = analysisTimestamp; }
    }
}
