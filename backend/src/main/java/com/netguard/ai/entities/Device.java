package com.netguard.ai.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String hostname;

    @Column(nullable = false, unique = true)
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceType deviceType;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceStatus status;

    private Double cpuUsage;

    private Double memoryUsage;

    private Double packetLoss;

    private Double latency;

    private LocalDateTime lastSeen;

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = DeviceStatus.ONLINE;
        }
        if (lastSeen == null) {
            lastSeen = LocalDateTime.now();
        }
    }

    public enum DeviceType {
        ROUTER, SWITCH, SERVER, FIREWALL, LOAD_BALANCER
    }

    public enum DeviceStatus {
        ONLINE, WARNING, OFFLINE, CRITICAL
    }
}
