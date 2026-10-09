package com.netguard.ai.repositories;

import com.netguard.ai.entities.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    List<Prediction> findByDeviceId(Long deviceId);

    List<Prediction> findByRiskLevel(Prediction.RiskLevel riskLevel);

    Optional<Prediction> findTopByDeviceIdOrderByTimestampDesc(Long deviceId);

    @Query("SELECT p FROM Prediction p WHERE p.riskLevel IN ('HIGH', 'CRITICAL')")
    List<Prediction> findHighRiskPredictions();

    @Query("SELECT COUNT(p) FROM Prediction p WHERE p.riskLevel = :riskLevel")
    long countByRiskLevel(Prediction.RiskLevel riskLevel);
}
