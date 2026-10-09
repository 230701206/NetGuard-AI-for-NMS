package com.netguard.ai.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType alertType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    private String source;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertStatus status;

    private Boolean correlated;

    private Long correlationGroupId;

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = AlertStatus.OPEN;
        }
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        if (correlated == null) {
            correlated = false;
        }
    }

    public enum AlertType {
        INTERFACE_DOWN, PACKET_LOSS, HIGH_LATENCY, CPU_HIGH, MEMORY_HIGH,
        CONNECTION_TIMEOUT, SERVICE_DOWN, DISK_FULL, BANDWIDTH_HIGH,
        ROUTING_ERROR, AUTHENTICATION_FAILURE, PORT_FLAPPING
    }

    public enum Severity {
        INFO, WARNING, ERROR, CRITICAL
    }

    public enum AlertStatus {
        OPEN, ACKNOWLEDGED, RESOLVED, CLOSED
    }
}
