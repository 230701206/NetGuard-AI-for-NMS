package com.netguard.ai.services;

import com.netguard.ai.dtos.AlertDTO;
import com.netguard.ai.dtos.CreateAlertDTO;
import com.netguard.ai.entities.Alert;
import com.netguard.ai.entities.Device;
import com.netguard.ai.exception.ResourceNotFoundException;
import com.netguard.ai.repositories.AlertRepository;
import com.netguard.ai.repositories.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final DeviceRepository deviceRepository;

    public List<AlertDTO> getAllAlerts() {
        return alertRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AlertDTO getAlertById(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert", id));
        return convertToDTO(alert);
    }

    public AlertDTO createAlert(CreateAlertDTO createAlertDTO) {
        Device device = deviceRepository.findById(createAlertDTO.getDeviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Device", createAlertDTO.getDeviceId()));

        Alert alert = new Alert();
        alert.setDevice(device);
        alert.setAlertType(Alert.AlertType.valueOf(createAlertDTO.getAlertType().toUpperCase()));
        alert.setSeverity(Alert.Severity.valueOf(createAlertDTO.getSeverity().toUpperCase()));
        alert.setMessage(createAlertDTO.getMessage());
        alert.setSource(createAlertDTO.getSource());
        alert.setTimestamp(LocalDateTime.now());
        alert.setStatus(Alert.AlertStatus.OPEN);
        alert.setCorrelated(false);

        return convertToDTO(alertRepository.save(alert));
    }

    public AlertDTO updateAlert(Long id, Alert alertDetails) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert", id));

        alert.setSeverity(alertDetails.getSeverity());
        alert.setStatus(alertDetails.getStatus());
        alert.setCorrelated(alertDetails.getCorrelated());
        alert.setCorrelationGroupId(alertDetails.getCorrelationGroupId());

        return convertToDTO(alertRepository.save(alert));
    }

    public void deleteAlert(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert", id));
        alertRepository.delete(alert);
    }

    public List<AlertDTO> getAlertsByDeviceId(Long deviceId) {
        return alertRepository.findByDeviceId(deviceId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AlertDTO> getRecentAlerts(int hours) {
        LocalDateTime startTime = LocalDateTime.now().minusHours(hours);
        return alertRepository.findRecentAlerts(startTime).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AlertDTO> getUncorrelatedAlerts() {
        return alertRepository.findUncorrelatedAlerts().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public long getCriticalAlertCount() {
        return alertRepository.countBySeverityAndStatus(
                Alert.Severity.CRITICAL, 
                Alert.AlertStatus.OPEN
        );
    }

    private AlertDTO convertToDTO(Alert alert) {
        AlertDTO dto = new AlertDTO();
        dto.setId(alert.getId());
        dto.setDeviceId(alert.getDevice().getId());
        dto.setDeviceHostname(alert.getDevice().getHostname());
        dto.setAlertType(alert.getAlertType());
        dto.setSeverity(alert.getSeverity());
        dto.setMessage(alert.getMessage());
        dto.setSource(alert.getSource());
        dto.setTimestamp(alert.getTimestamp());
        dto.setStatus(alert.getStatus());
        dto.setCorrelated(alert.getCorrelated());
        dto.setCorrelationGroupId(alert.getCorrelationGroupId());
        return dto;
    }
}
