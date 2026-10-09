package com.netguard.ai.dtos;

import com.netguard.ai.entities.Alert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertDTO {
    private Long id;
    private Long deviceId;
    private String deviceHostname;
    private Alert.AlertType alertType;
    private Alert.Severity severity;
    private String message;
    private String source;
    private LocalDateTime timestamp;
    private Alert.AlertStatus status;
    private Boolean correlated;
    private Long correlationGroupId;
}
