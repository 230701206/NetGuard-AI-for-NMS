# NetGuard AI

## Predictive Alert-Correlation and Root-Cause Assistant for Network Management Systems

### A Final Year College Project

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Abstract](#abstract)
3. [Problem Statement](#problem-statement)
4. [Existing System](#existing-system)
5. [Proposed System](#proposed-system)
6. [Objectives](#objectives)
7. [Features](#features)
8. [System Architecture](#system-architecture)
9. [Technologies Used](#technologies-used)
10. [Database Design](#database-design)
11. [AI/ML Methodology](#aiml-methodology)
12. [Alert Correlation Algorithm](#alert-correlation-algorithm)
13. [Root Cause Analysis Algorithm](#root-cause-analysis-algorithm)
14. [Installation Guide](#installation-guide)
15. [API Documentation](#api-documentation)
16. [Demo Instructions](#demo-instructions)
17. [Future Enhancements](#future-enhancements)
18. [Conclusion](#conclusion)

---

## Project Overview

NetGuard AI is an intelligent Network Management System assistant that uses artificial intelligence and machine learning to analyze network alerts, correlate related events, identify root causes, and predict potential failures before they occur. Instead of overwhelming network administrators with hundreds of independent alerts, NetGuard AI groups related alerts into incidents, identifies probable root causes, and provides actionable recommendations.

### Key Capabilities

- **Alert Correlation**: Groups related alerts based on time proximity, device topology, and alert patterns
- **Root Cause Analysis**: AI-powered identification of probable root causes with confidence scores
- **Predictive Analytics**: Machine learning models predict device failures before they occur
- **Real-time Dashboard**: Live monitoring with charts, metrics, and alert feeds
- **Incident Management**: Track and resolve network incidents with AI recommendations
- **Simulation Mode**: Simulate network failures for testing and demonstration

---

## Abstract

Network Management Systems generate hundreds of alerts daily from routers, switches, servers, and other network devices. Traditional monitoring tools present these alerts independently, making it difficult for network administrators to identify relationships between events and determine root causes. This project presents NetGuard AI, an intelligent system that uses artificial intelligence and machine learning to correlate alerts, identify root causes, and predict potential failures. The system collects network alerts, stores them in a database, analyzes alert severity, correlates related alerts into incidents, identifies probable root causes, and provides confidence scores. Additionally, it uses machine learning to predict future failures based on device metrics and historical data. The system provides a modern web-based dashboard for real-time monitoring and analysis. Implemented using Spring Boot, React, MySQL, and Python/scikit-learn, NetGuard AI demonstrates how AI can transform network operations from reactive to proactive.

---

## Problem Statement

Modern network infrastructure generates massive volumes of alerts from diverse devices including routers, switches, servers, firewalls, and load balancers. Traditional Network Management Systems (NMS) present these alerts as independent events, leading to several critical problems:

1. **Alert Fatigue**: Network operators receive hundreds of alerts daily, making it impossible to investigate each one thoroughly
2. **Lack of Context**: Alerts are shown without relationships to other events, missing patterns that indicate larger problems
3. **Reactive Approach**: Problems are addressed only after they occur, leading to downtime and service disruption
4. **Inefficient Troubleshooting**: Without correlation, operators spend significant time determining relationships between alerts
5. **No Predictive Capability**: Traditional systems cannot predict failures before they happen
6. **Root Cause Ambiguity**: When multiple devices report issues, identifying the actual root cause is challenging

These problems result in increased Mean Time To Repair (MTTR), reduced network availability, and higher operational costs.

---

## Existing System

Current Network Management Systems typically include:

- **SNMP-based Monitoring**: Periodic polling of device metrics (CPU, memory, interfaces)
- **Threshold-based Alerting**: Alerts generated when metrics exceed configured thresholds
- **Event Logs**: Storage of all events and alerts in log files or databases
- **Basic Dashboards**: Visualization of current device status and recent alerts
- **Manual Correlation**: Operators must manually determine relationships between alerts
- **No AI/ML**: Lack of intelligent analysis or predictive capabilities

### Limitations

- No automatic alert correlation
- No root cause analysis
- No predictive failure detection
- High operator workload
- Slow incident resolution
- Reactive rather than proactive

---

## Proposed System

NetGuard AI addresses these limitations through an intelligent, AI-powered approach:

### Core Components

1. **Alert Collection**: Receives alerts from network devices via REST API
2. **Alert Correlation Engine**: Groups related alerts based on multiple factors
3. **Root Cause Analysis**: Identifies probable root causes using rule-based scoring
4. **Prediction Service**: ML-based failure risk prediction for each device
5. **Incident Management**: Creates and tracks incidents from correlated alerts
6. **Dashboard**: Real-time visualization of network health, alerts, and predictions

### Key Innovations

- **Multi-factor Correlation**: Considers time proximity, device topology, alert types, and historical patterns
- **Explainable AI**: Provides confidence scores and explanations for root cause analysis
- **Predictive Analytics**: Uses machine learning to predict failures before they occur
- **Simulation Mode**: Allows testing and demonstration without real network infrastructure
- **Modern UI**: Professional, dark-themed dashboard with real-time updates

---

## Objectives

### Primary Objectives

1. Develop an intelligent alert correlation system that groups related alerts into incidents
2. Implement root cause analysis to identify the most probable cause of network issues
3. Create a predictive analytics system using machine learning to forecast device failures
4. Design a modern, user-friendly dashboard for real-time network monitoring
5. Provide simulation capabilities for testing and demonstration

### Secondary Objectives

1. Reduce alert fatigue by grouping related alerts
2. Decrease Mean Time To Repair (MTTR) through faster root cause identification
3. Enable proactive network management through predictive analytics
4. Provide explainable AI results with confidence scores
5. Create a system that can run locally without complex infrastructure

---

## Features

### 1. Dashboard
- Real-time network health overview
- Summary cards showing total devices, active devices, critical alerts, open incidents
- Alert severity distribution charts
- Device health visualization
- Live alert feed
- Network health percentage calculation

### 2. Device Management
- Complete device inventory
- Device status monitoring (Online, Warning, Offline, Critical)
- Real-time metrics: CPU, Memory, Latency, Packet Loss
- AI risk level display per device
- Failure probability predictions

### 3. Alert Management
- Comprehensive alert listing
- Severity-based filtering (Critical, Error, Warning, Info)
- Alert status tracking (Open, Acknowledged, Resolved, Closed)
- Historical alert view
- Device-specific alert history

### 4. Alert Correlation
- Automatic correlation of related alerts
- Time-based grouping (configurable time window)
- Device topology awareness
- Alert type pattern matching
- Correlation score calculation
- Manual correlation trigger

### 5. Incident Management
- Incident creation from correlated alerts
- Incident lifecycle management (Open, Investigating, Resolved, Closed)
- Root cause display
- Confidence scores
- AI recommendations
- Affected devices tracking

### 6. Root Cause Analysis
- AI-powered root cause identification
- Confidence score calculation
- Detailed explanation generation
- Timeline analysis
- Device dependency consideration
- Severity-weighted scoring

### 7. AI/ML Prediction
- Device failure probability prediction
- Risk level classification (Low, Medium, High, Critical)
- Predictive failure type identification
- Confidence metrics
- Actionable recommendations
- Feature importance analysis

### 8. Network Health Monitoring
- Real-time network metrics
- CPU and memory usage charts
- Network latency visualization
- Packet loss monitoring
- Device health tables
- Trend analysis

### 9. Simulation Features
- Router failure simulation
- Packet loss simulation
- High CPU simulation
- Interface failure simulation
- Server timeout simulation
- Random alert generation
- Complete workflow demonstration

### 10. System Settings
- System information display
- Architecture overview
- Technology stack documentation
- Demo instructions
- Configuration details

---

## System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Frontend                              │
│                    (React + Vite)                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │  Dashboard   │  │   Devices    │  │   Alerts     │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │  Incidents   │  │ Root Cause   │  │ Predictions  │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
└───────────────────────────┬─────────────────────────────────┘
                            │ REST API
                            │
┌───────────────────────────┴─────────────────────────────────┐
│                    Backend (Spring Boot)                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │   Controllers │  │   Services   │  │  Repositories│     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │ Correlation  │  │    RCA       │  │  Prediction  │     │
│  │    Engine    │  │   Engine     │  │   Service    │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
└───────────────────────────┬─────────────────────────────────┘
                            │
         ┌──────────────────┼──────────────────┐
         │                  │                  │
┌────────┴────────┐  ┌───────┴────────┐  ┌──────┴────────┐
│   MySQL 8      │  │  ML Service    │  │   Data Init   │
│   Database     │  │  (Python)      │  │   (Sample)    │
└─────────────────┘  └────────────────┘  └────────────────┘
```

### Data Flow

1. **Alert Ingestion**: Frontend sends alerts via REST API to backend
2. **Storage**: Backend stores alerts in MySQL database
3. **Correlation**: Correlation engine processes uncorrelated alerts
4. **Incident Creation**: Related alerts grouped into incidents
5. **RCA**: Root cause analysis identifies probable causes
6. **Prediction**: ML service predicts failure risks
7. **Dashboard**: Frontend displays results in real-time

---

## Technologies Used

### Backend
- **Java 17**: Programming language
- **Spring Boot 3.2.0**: Application framework
- **Spring Data JPA**: ORM and database access
- **Spring Validation**: Input validation
- **Spring WebSocket**: Real-time communication (optional)
- **Lombok**: Reduce boilerplate code
- **Maven**: Build and dependency management
- **MySQL Connector**: Database connectivity

### Frontend
- **React 18**: UI framework
- **Vite**: Build tool and dev server
- **React Router**: Client-side routing
- **Tailwind CSS**: Styling
- **Recharts**: Data visualization
- **Axios**: HTTP client
- **Lucide React**: Icon library

### AI/ML
- **Python 3.x**: ML service language
- **FastAPI**: ML service framework
- **scikit-learn**: Machine learning library
- **pandas**: Data manipulation
- **numpy**: Numerical computing
- **joblib**: Model serialization

### Database
- **MySQL 8**: Relational database
- **Hibernate ORM**: Object-relational mapping

---

## Database Design

### Entity Relationships

```
Device (1) ----< Alert (N)
Device (1) ----< Prediction (N)
Device (1) ----< NetworkMetric (N)
Alert (N) ----< Incident (1) [via correlation_group_id]
```

### Tables

#### Devices
- `id`: Primary key
- `hostname`: Unique device name
- `ip_address`: Unique IP address
- `device_type`: Router, Switch, Server, Firewall, Load Balancer
- `location`: Physical location
- `status`: Online, Warning, Offline, Critical
- `cpu_usage`: Current CPU percentage
- `memory_usage`: Current memory percentage
- `packet_loss`: Packet loss percentage
- `latency`: Network latency in ms
- `last_seen`: Last update timestamp

#### Alerts
- `id`: Primary key
- `device_id`: Foreign key to Devices
- `alert_type`: Interface Down, Packet Loss, High Latency, etc.
- `severity`: Info, Warning, Error, Critical
- `message`: Alert description
- `source`: Alert source (SNMP, Application, etc.)
- `timestamp`: Alert creation time
- `status`: Open, Acknowledged, Resolved, Closed
- `correlated`: Boolean flag
- `correlation_group_id`: Links alerts to incidents

#### Incidents
- `id`: Primary key
- `title`: Incident title
- `severity`: Info, Warning, Error, Critical
- `status`: Open, Investigating, Resolved, Closed
- `root_cause`: Identified root cause
- `root_cause_device`: Device causing the issue
- `confidence_score`: RCA confidence percentage
- `affected_devices`: List of affected devices
- `created_at`: Incident creation time
- `resolved_at`: Incident resolution time
- `ai_recommendation`: AI-generated recommendation

#### Predictions
- `id`: Primary key
- `device_id`: Foreign key to Devices
- `failure_probability`: Predicted failure probability (0-1)
- `risk_level`: Low, Medium, High, Critical
- `predicted_failure`: Type of predicted failure
- `confidence`: Model confidence
- `timestamp`: Prediction time
- `recommendation`: Actionable recommendation

#### Network Metrics
- `id`: Primary key
- `device_id`: Foreign key to Devices
- `cpu_usage`: CPU percentage
- `memory_usage`: Memory percentage
- `latency`: Network latency
- `packet_loss`: Packet loss percentage
- `interface_errors`: Interface error count
- `timestamp`: Metric collection time

---

## AI/ML Methodology

### Prediction Model

#### Features Used
1. **CPU Usage**: Current CPU utilization percentage
2. **Memory Usage**: Current memory utilization percentage
3. **Packet Loss**: Network packet loss percentage
4. **Latency**: Network latency in milliseconds
5. **Recent Critical Alerts**: Count of critical alerts in last 24 hours
6. **Interface Errors**: Number of interface errors

#### Model Selection
- **Algorithm**: Random Forest Classifier
- **Justification**: Handles non-linear relationships, provides feature importance, robust to outliers
- **Training Data**: Synthetic data generated based on realistic failure patterns

#### Risk Classification
- **LOW**: Failure probability < 25%
- **MEDIUM**: Failure probability 25-50%
- **HIGH**: Failure probability 50-75%
- **CRITICAL**: Failure probability > 75%

#### Fallback Mechanism
If ML service is unavailable, the system uses a rule-based scoring algorithm:
- CPU > 80%: +20 points
- Memory > 80%: +15 points
- Packet Loss > 5%: +15 points
- Latency > 100ms: +10 points
- Recent Critical Alerts: +10 points each
- Score >= 50: HIGH/CRITICAL risk

---

## Alert Correlation Algorithm

### Correlation Factors

1. **Time Proximity** (40% weight)
   - Alerts within configurable time window (default: 5 minutes)
   - Earlier alerts receive higher scores

2. **Device Topology** (25% weight)
   - Router → Switch → Server relationships
   - Same device correlations
   - Physical network connections

3. **Alert Type Relationships** (25% weight)
   - Interface Down → Packet Loss → High Latency → Connection Timeout
   - Predefined alert type patterns
   - Severity consideration

4. **Historical Patterns** (10% weight)
   - Repeated alert combinations
   - Known failure patterns

### Correlation Score Calculation

```
Score = (TimeScore × 0.4) + (DeviceScore × 0.25) +
        (AlertTypeScore × 0.25) + (HistoricalScore × 0.1)
```

### Correlation Process

1. Fetch uncorrelated alerts
2. Identify critical alerts as potential base alerts
3. For each base alert:
   - Find alerts within time window
   - Filter by device relationship
   - Filter by alert type relationship
   - Calculate correlation score
   - If score >= threshold (default: 60), create correlation group
4. Mark correlated alerts
5. Create incident from correlation group

---

## Root Cause Analysis Algorithm

### RCA Factors

1. **Temporal Position** (40% weight)
   - Earliest alerts in incident timeline
   - Time difference from first alert

2. **Severity** (25% weight)
   - Critical severity: 100 points
   - Error severity: 85 points
   - Warning severity: 60 points
   - Info severity: 30 points

3. **Device Topology** (25% weight)
   - Router failures: 100 points (affects downstream)
   - Switch failures: 85 points (affects servers)
   - Server failures: 50 points (end device)

4. **Alert Type** (10% weight)
   - Interface Down: 100 points
   - Routing Error: 95 points
   - Packet Loss: 80 points
   - High Latency: 70 points
   - Connection Timeout: 60 points
   - Service Down: 50 points

### RCA Score Calculation

```
RCA Score = (TemporalScore × 0.4) + (SeverityScore × 0.25) +
            (TopologyScore × 0.25) + (AlertTypeScore × 0.1)
```

### Explanation Generation

The system generates human-readable explanations including:
- Identified root cause device and type
- Confidence score
- Reasoning:
  - Earliest event in timeline
  - Critical severity
  - Device topology impact
  - Downstream alert count
  - Alert type significance

---

## Installation Guide

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher
- Node.js 18 or higher
- Python 3.8 or higher (for ML service)
- Git

### STEP 1: Install Java

**Windows:**
1. Download Java 17 from https://www.oracle.com/java/technologies/downloads/
2. Run the installer
3. Set JAVA_HOME environment variable
4. Add Java bin to PATH

**Verify:**
```bash
java -version
```

### STEP 2: Install Maven

**Windows:**
1. Download Maven from https://maven.apache.org/download.cgi
2. Extract to a directory (e.g., C:\Program Files\Apache\maven)
3. Set MAVEN_HOME environment variable
4. Add Maven bin to PATH

**Verify:**
```bash
mvn -version
```

### STEP 3: Install MySQL

**Windows:**
1. Download MySQL 8 from https://dev.mysql.com/downloads/mysql/
2. Run the installer
3. Set root password (remember this for configuration)
4. Start MySQL service

**Verify:**
```bash
mysql --version
```

### STEP 4: Create Database

1. Open MySQL Command Line Client
2. Run:
```sql
CREATE DATABASE netguard_ai;
```

Or use the provided schema:
```bash
mysql -u root -p < database/schema.sql
```

### STEP 5: Configure Backend

1. Navigate to backend directory:
```bash
cd netguard-ai/backend
```

2. Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/netguard_ai?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### STEP 6: Start Backend

1. From backend directory:
```bash
mvn clean install
mvn spring-boot:run
```

Backend will start on http://localhost:8080

### STEP 7: Start ML Service (Optional)

1. Navigate to ml-service directory:
```bash
cd netguard-ai/ml-service
```

2. Install Python dependencies:
```bash
pip install -r requirements.txt
```

3. Train the model:
```bash
python train_model.py
```

4. Start the ML service:
```bash
python app.py
```

ML service will start on http://localhost:5000

**Note:** If ML service is not started, the backend will use fallback rule-based predictions.

### STEP 8: Start Frontend

1. Navigate to frontend directory:
```bash
cd netguard-ai/frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start development server:
```bash
npm run dev
```

Frontend will start on http://localhost:5173

### STEP 9: Open Dashboard

Open browser and navigate to:
```
http://localhost:5173
```

The dashboard will load with pre-populated sample data.

---

## API Documentation

### Device Endpoints

#### GET /api/devices
Get all devices

**Response:**
```json
[
  {
    "id": 1,
    "hostname": "Router-01",
    "ipAddress": "192.168.1.1",
    "deviceType": "ROUTER",
    "status": "ONLINE",
    "cpuUsage": 45.0,
    "memoryUsage": 52.0,
    "riskLevel": "LOW"
  }
]
```

#### GET /api/devices/{id}
Get device by ID

#### POST /api/devices
Create new device

#### PUT /api/devices/{id}
Update device

#### DELETE /api/devices/{id}
Delete device

### Alert Endpoints

#### GET /api/alerts
Get all alerts

#### GET /api/alerts/{id}
Get alert by ID

#### POST /api/alerts
Create new alert

**Request:**
```json
{
  "deviceId": 1,
  "alertType": "INTERFACE_DOWN",
  "severity": "CRITICAL",
  "message": "Interface Gi0/1 is down",
  "source": "SNMP"
}
```

#### GET /api/alerts/recent?hours=24
Get recent alerts

### Incident Endpoints

#### GET /api/incidents
Get all incidents

#### GET /api/incidents/{id}
Get incident by ID

#### POST /api/incidents/{id}/resolve
Resolve incident

### Correlation Endpoints

#### POST /api/correlation/run
Run alert correlation

**Response:**
```json
{
  "success": true,
  "message": "Correlation completed",
  "correlatedGroups": 2
}
```

### Root Cause Endpoints

#### GET /api/root-cause/incident/{incidentId}
Analyze root cause for incident

**Response:**
```json
{
  "rootCauseDevice": "Router-01",
  "rootCauseType": "INTERFACE_DOWN",
  "confidenceScore": 91.5,
  "explanation": "Router-01 interface failure is the most probable root cause..."
}
```

### Prediction Endpoints

#### GET /api/predictions
Get all predictions

#### GET /api/predictions/device/{deviceId}
Get prediction for device

#### POST /api/predictions/run
Run predictions for all devices

### Simulation Endpoints

#### POST /api/simulation/router-failure
Simulate router failure

#### POST /api/simulation/packet-loss
Simulate packet loss

#### POST /api/simulation/high-cpu
Simulate high CPU

#### POST /api/simulation/random-alerts?count=5
Generate random alerts

### Dashboard Endpoints

#### GET /api/dashboard/summary
Get dashboard summary

**Response:**
```json
{
  "totalDevices": 7,
  "activeDevices": 6,
  "criticalAlerts": 3,
  "openIncidents": 2,
  "highRiskDevices": 1,
  "networkHealthPercentage": 78.5
}
```

#### GET /api/dashboard/alerts?limit=10
Get recent alerts for dashboard

#### GET /api/dashboard/incidents?limit=5
Get recent incidents for dashboard

---

## Demo Instructions

See [demo-guide.md](demo-guide.md) for detailed demonstration instructions.

### Quick Demo

1. Start all services (Backend, Frontend, optional ML service)
2. Open http://localhost:5173
3. View dashboard with pre-populated data
4. Click "Simulate Router Failure" button
5. Watch alerts being generated, correlated, and analyzed
6. Navigate to Incidents page to view the created incident
7. Click "Analyze Root Cause" to see AI analysis
8. Navigate to Predictions page to view ML predictions

---

## Future Enhancements

1. **Real WebSocket Support**: True real-time updates without polling
2. **Advanced ML Models**: Deep learning models for better predictions
3. **Topology Visualization**: Interactive network topology map
4. **Multi-tenant Support**: Support for multiple organizations
5. **Authentication & Authorization**: Role-based access control
6. **Mobile App**: Native mobile application
7. **Integration APIs**: Integration with popular NMS tools
8. **Historical Analytics**: Advanced historical trend analysis
9. **Automated Remediation**: Automated response to certain alerts
10. **Natural Language Processing**: Query system using natural language

---

## Conclusion

NetGuard AI successfully demonstrates how artificial intelligence and machine learning can transform network management from reactive to proactive. By correlating alerts, identifying root causes, and predicting failures, the system significantly reduces alert fatigue, decreases Mean Time To Repair, and enables network administrators to focus on strategic tasks rather than repetitive troubleshooting. The project showcases the practical application of AI in network operations and provides a foundation for further research and development in intelligent network management systems.

---

## Contact

For questions or support, please refer to the demo-guide.md or contact the project team.

---

**NetGuard AI v1.0.0**
*Final Year College Project*
