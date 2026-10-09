package com.netguard.ai.services;

import com.netguard.ai.dtos.DeviceDTO;
import com.netguard.ai.entities.Device;
import com.netguard.ai.entities.Prediction;
import com.netguard.ai.exception.ResourceNotFoundException;
import com.netguard.ai.repositories.DeviceRepository;
import com.netguard.ai.repositories.PredictionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final PredictionRepository predictionRepository;

    public List<DeviceDTO> getAllDevices() {
        return deviceRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO getDeviceById(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device", id));
        return convertToDTO(device);
    }

    public DeviceDTO createDevice(Device device) {
        return convertToDTO(deviceRepository.save(device));
    }

    public DeviceDTO updateDevice(Long id, Device deviceDetails) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device", id));

        device.setHostname(deviceDetails.getHostname());
        device.setIpAddress(deviceDetails.getIpAddress());
        device.setDeviceType(deviceDetails.getDeviceType());
        device.setLocation(deviceDetails.getLocation());
        device.setStatus(deviceDetails.getStatus());
        device.setCpuUsage(deviceDetails.getCpuUsage());
        device.setMemoryUsage(deviceDetails.getMemoryUsage());
        device.setPacketLoss(deviceDetails.getPacketLoss());
        device.setLatency(deviceDetails.getLatency());
        device.setLastSeen(deviceDetails.getLastSeen());

        return convertToDTO(deviceRepository.save(device));
    }

    public void deleteDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device", id));
        deviceRepository.delete(device);
    }

    public List<DeviceDTO> getActiveDevices() {
        return deviceRepository.findActiveDevices().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public long getTotalDevices() {
        return deviceRepository.count();
    }

    public long getActiveDeviceCount() {
        return deviceRepository.countByStatus(Device.DeviceStatus.ONLINE);
    }

    private DeviceDTO convertToDTO(Device device) {
        DeviceDTO dto = new DeviceDTO();
        dto.setId(device.getId());
        dto.setHostname(device.getHostname());
        dto.setIpAddress(device.getIpAddress());
        dto.setDeviceType(device.getDeviceType());
        dto.setLocation(device.getLocation());
        dto.setStatus(device.getStatus());
        dto.setCpuUsage(device.getCpuUsage());
        dto.setMemoryUsage(device.getMemoryUsage());
        dto.setPacketLoss(device.getPacketLoss());
        dto.setLatency(device.getLatency());
        dto.setLastSeen(device.getLastSeen() != null 
                ? device.getLastSeen().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) 
                : null);

        Optional<Prediction> latestPrediction = predictionRepository.findTopByDeviceIdOrderByTimestampDesc(device.getId());
        latestPrediction.ifPresent(pred -> {
            dto.setRiskLevel(pred.getRiskLevel());
            dto.setFailureProbability(pred.getFailureProbability());
        });

        return dto;
    }
}
