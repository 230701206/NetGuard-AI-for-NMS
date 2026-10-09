package com.netguard.ai.services;

import com.netguard.ai.dtos.AlertDTO;
import com.netguard.ai.dtos.IncidentDTO;
import com.netguard.ai.entities.Alert;
import com.netguard.ai.entities.Incident;
import com.netguard.ai.exception.ResourceNotFoundException;
import com.netguard.ai.repositories.AlertRepository;
import com.netguard.ai.repositories.IncidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final AlertRepository alertRepository;

    public List<IncidentDTO> getAllIncidents() {
        return incidentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public IncidentDTO getIncidentById(Long id) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident", id));
        return convertToDTO(incident);
    }

    public IncidentDTO createIncident(Incident incident) {
        return convertToDTO(incidentRepository.save(incident));
    }

    @Transactional
    public IncidentDTO resolveIncident(Long id) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident", id));

        incident.setStatus(Incident.IncidentStatus.RESOLVED);
        incident.setResolvedAt(LocalDateTime.now());

        return convertToDTO(incidentRepository.save(incident));
    }

    public List<IncidentDTO> getOpenIncidents() {
        return incidentRepository.findOpenIncidents().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public long getOpenIncidentCount() {
        return incidentRepository.countOpenIncidents();
    }

    @Transactional
    public IncidentDTO createIncidentFromAlerts(List<Alert> alerts, String rootCause, String rootCauseDevice, double confidenceScore) {
        Incident incident = new Incident();

        Alert firstAlert = alerts.get(0);
        incident.setTitle(generateIncidentTitle(alerts));
        incident.setSeverity(firstAlert.getSeverity());
        incident.setStatus(Incident.IncidentStatus.OPEN);
        incident.setRootCause(rootCause);
        incident.setRootCauseDevice(rootCauseDevice);
        incident.setConfidenceScore(confidenceScore);
        incident.setAffectedDevices(alerts.stream()
                .map(a -> a.getDevice().getHostname())
                .distinct()
                .collect(Collectors.joining(", ")));
        incident.setCreatedAt(LocalDateTime.now());
        incident.setAiRecommendation(generateRecommendation(rootCause, rootCauseDevice));

        Incident savedIncident = incidentRepository.save(incident);

        Long groupId = savedIncident.getId();
        for (Alert alert : alerts) {
            alert.setCorrelated(true);
            alert.setCorrelationGroupId(groupId);
        }
        alertRepository.saveAll(alerts);

        return convertToDTO(savedIncident);
    }

    private String generateIncidentTitle(List<Alert> alerts) {
        Alert firstAlert = alerts.get(0);
        return String.format("%s - %s on %s",
                firstAlert.getSeverity(),
                firstAlert.getAlertType(),
                firstAlert.getDevice().getHostname()
        );
    }

    private String generateRecommendation(String rootCause, String rootCauseDevice) {
        return String.format(
                "Investigate %s on %s. Check device logs, interface status, and network connectivity.",
                rootCause,
                rootCauseDevice
        );
    }

    private IncidentDTO convertToDTO(Incident incident) {
        IncidentDTO dto = new IncidentDTO();
        dto.setId(incident.getId());
        dto.setTitle(incident.getTitle());
        dto.setSeverity(incident.getSeverity());
        dto.setStatus(incident.getStatus());
        dto.setRootCause(incident.getRootCause());
        dto.setRootCauseDevice(incident.getRootCauseDevice());
        dto.setConfidenceScore(incident.getConfidenceScore());
        dto.setAffectedDevices(incident.getAffectedDevices());
        dto.setCreatedAt(incident.getCreatedAt());
        dto.setResolvedAt(incident.getResolvedAt());
        dto.setAiRecommendation(incident.getAiRecommendation());
        dto.setRelatedAlerts(List.of());

        return dto;
    }
}
