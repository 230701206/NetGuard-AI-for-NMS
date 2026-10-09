package com.netguard.ai.dtos;

import com.netguard.ai.entities.Alert;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAlertDTO {
    @NotNull
    private Long deviceId;

    @NotNull
    private String alertType;

    @NotNull
    private String severity;

    @NotNull
    private String message;

    private String source;
}
