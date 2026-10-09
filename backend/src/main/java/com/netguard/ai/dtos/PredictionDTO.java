package com.netguard.ai.dtos;

import com.netguard.ai.entities.Prediction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionDTO {
    private Long id;
    private Long deviceId;
    private String deviceHostname;
    private Double failureProbability;
    private Prediction.RiskLevel riskLevel;
    private String predictedFailure;
    private Double confidence;
    private LocalDateTime timestamp;
    private String recommendation;
}
