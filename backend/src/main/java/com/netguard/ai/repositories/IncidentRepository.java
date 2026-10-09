package com.netguard.ai.repositories;

import com.netguard.ai.entities.Incident;
import com.netguard.ai.entities.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findByStatus(Incident.IncidentStatus status);

    List<Incident> findBySeverity(Alert.Severity severity);

    @Query("SELECT i FROM Incident i WHERE i.status != 'CLOSED' ORDER BY i.createdAt DESC")
    List<Incident> findOpenIncidents();

    @Query("SELECT COUNT(i) FROM Incident i WHERE i.status = 'OPEN'")
    long countOpenIncidents();
}
