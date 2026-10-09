package com.netguard.ai.dtos;

import com.netguard.ai.entities.Device;
import com.netguard.ai.entities.Prediction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDTO {
    private Long id;
    private String hostname;
    private String ipAddress;
    private Device.DeviceType deviceType;
    private String location;
    private Device.DeviceStatus status;
    private Double cpuUsage;
    private Double memoryUsage;
    private Double packetLoss;
    private Double latency;
    private String lastSeen;
    private Prediction.RiskLevel riskLevel;
    private Double failureProbability;
}
