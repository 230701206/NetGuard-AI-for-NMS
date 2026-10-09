package com.netguard.ai.dtos;

import com.netguard.ai.entities.Alert;
import com.netguard.ai.entities.Incident;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentDTO {
    private Long id;
    private String title;
    private Alert.Severity severity;
    private Incident.IncidentStatus status;
    private String rootCause;
    private String rootCauseDevice;
    private Double confidenceScore;
    private String affectedDevices;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
    private String aiRecommendation;
    private List<AlertDTO> relatedAlerts;
}
