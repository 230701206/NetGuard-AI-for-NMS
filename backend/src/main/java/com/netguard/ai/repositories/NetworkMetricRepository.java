package com.netguard.ai.repositories;

import com.netguard.ai.entities.NetworkMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NetworkMetricRepository extends JpaRepository<NetworkMetric, Long> {

    Optional<NetworkMetric> findTopByDeviceIdOrderByTimestampDesc(Long deviceId);
}
