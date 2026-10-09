package com.netguard.ai.repositories;

import com.netguard.ai.entities.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByDeviceId(Long deviceId);

    List<Alert> findBySeverity(Alert.Severity severity);

    List<Alert> findByStatus(Alert.AlertStatus status);

    List<Alert> findByTimestampAfter(LocalDateTime timestamp);

    @Query("SELECT a FROM Alert a WHERE a.timestamp >= :startTime ORDER BY a.timestamp DESC")
    List<Alert> findRecentAlerts(LocalDateTime startTime);

    @Query("SELECT COUNT(a) FROM Alert a WHERE a.severity = :severity AND a.status = :status")
    long countBySeverityAndStatus(Alert.Severity severity, Alert.AlertStatus status);

    @Query("SELECT a FROM Alert a WHERE a.correlated = false OR a.correlated IS NULL")
    List<Alert> findUncorrelatedAlerts();
}
