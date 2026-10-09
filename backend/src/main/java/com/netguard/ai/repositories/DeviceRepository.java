package com.netguard.ai.repositories;

import com.netguard.ai.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    Optional<Device> findByHostname(String hostname);

    Optional<Device> findByIpAddress(String ipAddress);

    List<Device> findByStatus(Device.DeviceStatus status);

    List<Device> findByDeviceType(Device.DeviceType deviceType);

    @Query("SELECT d FROM Device d WHERE d.status != 'OFFLINE'")
    List<Device> findActiveDevices();

    @Query("SELECT COUNT(d) FROM Device d WHERE d.status = :status")
    long countByStatus(Device.DeviceStatus status);
}
