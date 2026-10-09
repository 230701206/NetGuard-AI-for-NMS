-- NetGuard AI Database Schema
-- MySQL 8

CREATE DATABASE IF NOT EXISTS netguard_ai;
USE netguard_ai;

-- Devices Table
CREATE TABLE IF NOT EXISTS devices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hostname VARCHAR(255) NOT NULL UNIQUE,
    ip_address VARCHAR(45) NOT NULL UNIQUE,
    device_type ENUM('ROUTER', 'SWITCH', 'SERVER', 'FIREWALL', 'LOAD_BALANCER') NOT NULL,
    location VARCHAR(255),
    status ENUM('ONLINE', 'WARNING', 'OFFLINE', 'CRITICAL') NOT NULL DEFAULT 'ONLINE',
    cpu_usage DOUBLE,
    memory_usage DOUBLE,
    packet_loss DOUBLE,
    latency DOUBLE,
    last_seen DATETIME,
    INDEX idx_hostname (hostname),
    INDEX idx_status (status),
    INDEX idx_device_type (device_type)
);

-- Alerts Table
CREATE TABLE IF NOT EXISTS alerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_id BIGINT NOT NULL,
    alert_type ENUM('INTERFACE_DOWN', 'PACKET_LOSS', 'HIGH_LATENCY', 'CPU_HIGH', 'MEMORY_HIGH',
                    'CONNECTION_TIMEOUT', 'SERVICE_DOWN', 'DISK_FULL', 'BANDWIDTH_HIGH',
                    'ROUTING_ERROR', 'AUTHENTICATION_FAILURE', 'PORT_FLAPPING') NOT NULL,
    severity ENUM('INFO', 'WARNING', 'ERROR', 'CRITICAL') NOT NULL,
    message TEXT NOT NULL,
    source VARCHAR(255),
    timestamp DATETIME NOT NULL,
    status ENUM('OPEN', 'ACKNOWLEDGED', 'RESOLVED', 'CLOSED') NOT NULL DEFAULT 'OPEN',
    correlated BOOLEAN DEFAULT FALSE,
    correlation_group_id BIGINT,
    FOREIGN KEY (device_id) REFERENCES devices(id) ON DELETE CASCADE,
    INDEX idx_device_id (device_id),
    INDEX idx_severity (severity),
    INDEX idx_status (status),
    INDEX idx_timestamp (timestamp),
    INDEX idx_correlated (correlated)
);

-- Incidents Table
CREATE TABLE IF NOT EXISTS incidents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    severity ENUM('INFO', 'WARNING', 'ERROR', 'CRITICAL') NOT NULL,
    status ENUM('OPEN', 'INVESTIGATING', 'RESOLVED', 'CLOSED') NOT NULL DEFAULT 'OPEN',
    root_cause VARCHAR(255),
    root_cause_device VARCHAR(255),
    confidence_score DOUBLE,
    affected_devices TEXT,
    created_at DATETIME NOT NULL,
    resolved_at DATETIME,
    ai_recommendation TEXT,
    INDEX idx_status (status),
    INDEX idx_severity (severity),
    INDEX idx_created_at (created_at)
);

-- Predictions Table
CREATE TABLE IF NOT EXISTS predictions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_id BIGINT NOT NULL,
    failure_probability DOUBLE,
    risk_level ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL'),
    predicted_failure VARCHAR(255),
    confidence DOUBLE,
    timestamp DATETIME NOT NULL,
    recommendation TEXT,
    FOREIGN KEY (device_id) REFERENCES devices(id) ON DELETE CASCADE,
    INDEX idx_device_id (device_id),
    INDEX idx_risk_level (risk_level),
    INDEX idx_timestamp (timestamp)
);

-- Network Metrics Table
CREATE TABLE IF NOT EXISTS network_metrics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_id BIGINT NOT NULL,
    cpu_usage DOUBLE,
    memory_usage DOUBLE,
    latency DOUBLE,
    packet_loss DOUBLE,
    interface_errors INT,
    timestamp DATETIME NOT NULL,
    FOREIGN KEY (device_id) REFERENCES devices(id) ON DELETE CASCADE,
    INDEX idx_device_id (device_id),
    INDEX idx_timestamp (timestamp)
);
