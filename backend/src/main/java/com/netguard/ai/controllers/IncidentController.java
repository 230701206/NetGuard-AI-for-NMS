package com.netguard.ai.controllers;

import com.netguard.ai.dtos.IncidentDTO;
import com.netguard.ai.entities.Incident;
import com.netguard.ai.services.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class IncidentController {

    private final IncidentService incidentService;

    @GetMapping
    public ResponseEntity<List<IncidentDTO>> getAllIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentDTO> getIncidentById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getIncidentById(id));
    }

    @PostMapping
    public ResponseEntity<IncidentDTO> createIncident(@RequestBody Incident incident) {
        return ResponseEntity.ok(incidentService.createIncident(incident));
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<IncidentDTO> resolveIncident(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.resolveIncident(id));
    }

    @GetMapping("/open")
    public ResponseEntity<List<IncidentDTO>> getOpenIncidents() {
        return ResponseEntity.ok(incidentService.getOpenIncidents());
    }
}
