# NetGuard AI - Demo Guide

## Complete Demonstration Instructions for College Evaluators

This guide provides step-by-step instructions for demonstrating NetGuard AI to college evaluators. The demo should take approximately 5-10 minutes.

---

## Pre-Demo Preparation

### Checklist

- [ ] Java 17 installed and verified
- [ ] Maven installed and verified
- [ ] MySQL 8 installed and running
- [ ] Node.js 18+ installed
- [ ] Python 3.8+ installed (optional, for ML service)
- [ ] All services started successfully
- [ ] Browser ready at http://localhost:5173

### Quick Startup Sequence

1. **Start MySQL** (if not running)
2. **Start Backend**: `cd backend && mvn spring-boot:run`
3. **Start ML Service** (optional): `cd ml-service && python app.py`
4. **Start Frontend**: `cd frontend && npm run dev`
5. **Open Browser**: Navigate to http://localhost:5173

---

## Demo Script (5-10 Minutes)

### Part 1: Introduction (1 minute)

**Speech:**
"Good morning/afternoon. I would like to present NetGuard AI, a Predictive Alert-Correlation and Root-Cause Assistant for Network Management Systems. This project addresses the problem of alert fatigue in network operations by using artificial intelligence to correlate alerts, identify root causes, and predict failures before they occur."

**Actions:**
- Show the dashboard at http://localhost:5173
- Point out the modern, professional UI
- Mention the tech stack: Spring Boot, React, MySQL, Python/scikit-learn

---

### Part 2: Dashboard Overview (1 minute)

**Speech:**
"This is the main dashboard. As you can see, it provides a comprehensive overview of the network health. We have summary cards showing total devices, active devices, critical alerts, open incidents, high-risk devices, and overall network health percentage. The charts show device health distribution and alert severity. The live feed shows recent alerts in real-time."

**Actions:**
- Point to each summary card
- Explain the charts
- Show the recent alerts feed
- Mention that data is auto-refreshed every 10 seconds

---

### Part 3: Device Management (1 minute)

**Speech:**
"Let me show you the Devices page. Here we can see all network devices including routers, switches, servers, and firewalls. Each device card shows real-time metrics like CPU usage, memory usage, latency, and packet loss. Importantly, each device also displays an AI risk level and failure probability, which is calculated by our machine learning prediction service."

**Actions:**
- Navigate to Devices page
- Show device cards with metrics
- Point out risk levels (LOW, MEDIUM, HIGH, CRITICAL)
- Explain that this enables proactive maintenance

---

### Part 4: Alert Correlation Simulation (2 minutes) - **KEY DEMO**

**Speech:**
"Now, let me demonstrate the core feature of NetGuard AI. I will simulate a router failure. When a router interface goes down in a real network, it causes cascading issues on connected switches and servers. Traditional systems would show each alert separately. NetGuard AI correlates these alerts automatically."

**Actions:**
- Return to Dashboard
- Click "Simulate Router Failure" button
- Wait for simulation to complete (2-3 seconds)
- Point out the new alerts appearing
- Show that critical alerts count increased

**Speech:**
"As you can see, the simulation generated a realistic chain of alerts: Router interface down, followed by switch packet loss, high latency, and server connection timeouts. The system automatically correlated these alerts and created an incident."

---

### Part 5: Incident and Root Cause Analysis (2 minutes) - **KEY DEMO**

**Speech:**
"Let me navigate to the Incidents page to see the newly created incident. Here we can see Incident #1 with CRITICAL severity. The system has already identified a probable root cause with a confidence score."

**Actions:**
- Navigate to Incidents page
- Click on the new incident
- Show the incident details
- Point out the root cause, confidence score, and affected devices

**Speech:**
"Now, let me run the Root Cause Analysis to get the AI-powered explanation. The system analyzes the temporal sequence of alerts, device topology, alert severity, and alert types to determine the most probable root cause."

**Actions:**
- Click "Analyze Root Cause" button
- Wait for analysis to complete
- Show the AI analysis results
- Point out the confidence score (typically 85-95%)
- Read the explanation aloud

**Speech:**
"The system identified Router-01 interface failure as the root cause with 91% confidence. The explanation shows that it was the earliest critical event, it's a router which affects downstream devices, and multiple downstream alerts followed. This helps network operators quickly focus on the actual problem instead of investigating symptoms."

---

### Part 6: AI Predictions (1 minute)

**Speech:**
"Let me show you the Predictions page. Here we can see machine learning-based failure risk predictions for each device. The prediction service analyzes CPU usage, memory usage, packet loss, latency, recent critical alerts, and interface errors to calculate failure probability."

**Actions:**
- Navigate to Predictions page
- Show the risk distribution chart
- Point out device predictions with risk levels
- Explain the recommendations

**Speech:**
"Each prediction includes the failure probability, risk level, predicted failure type, confidence score, and actionable recommendation. This enables proactive maintenance - we can address issues before they cause actual failures."

---

### Part 7: Network Health (1 minute)

**Speech:**
"Finally, let me show the Network Health page. This provides real-time monitoring of network performance metrics with detailed charts and tables."

**Actions:**
- Navigate to Network Health page
- Show CPU and memory usage charts
- Show network latency chart
- Show packet loss chart
- Show device health details table

**Speech:**
"This comprehensive view helps operators quickly identify performance trends and potential issues before they escalate."

---

### Part 8: Technical Overview (30 seconds)

**Speech:**
"The system is built with a modern microservices architecture. The backend uses Spring Boot with Java 17, the database is MySQL 8, the frontend is React with Vite and Tailwind CSS, and the ML service uses Python with FastAPI and scikit-learn. The system includes intelligent alert correlation, root cause analysis, and predictive analytics capabilities."

**Actions:**
- Navigate to Settings page
- Show the system architecture section
- Show the technologies used section
- Show the key features section

---

### Part 9: Conclusion (30 seconds)

**Speech:**
"In summary, NetGuard AI transforms network management from reactive to proactive by using artificial intelligence to correlate alerts, identify root causes, and predict failures. This reduces alert fatigue, decreases Mean Time To Repair, and enables network administrators to focus on strategic tasks. The system is fully functional, can run locally without complex infrastructure, and demonstrates practical applications of AI in network operations."

**Actions:**
- Return to Dashboard
- Show the overall system one more time
- Thank the evaluators

---

## Short Speech for Viva

### Introduction (2-3 minutes)

"NetGuard AI is an intelligent Network Management System that addresses the critical problem of alert fatigue in network operations. Modern networks generate hundreds of alerts daily from routers, switches, servers, and other devices. Traditional monitoring tools present these alerts independently, making it extremely difficult for network operators to identify relationships between events and determine root causes.

Our solution uses artificial intelligence and machine learning to solve this problem through three core capabilities:

**First**, Alert Correlation: The system automatically groups related alerts based on time proximity, device topology relationships, alert type patterns, and historical data. For example, when a router interface fails, it causes packet loss on connected switches and connection timeouts on downstream servers. Our correlation engine identifies these relationships and groups them into a single incident.

**Second**, Root Cause Analysis: Once alerts are correlated, our AI-powered root cause analysis engine identifies the most probable cause. It analyzes the temporal sequence of alerts, considers device dependencies, evaluates alert severity, and examines network topology. It provides a confidence score and a detailed explanation, helping operators quickly focus on the actual problem.

**Third**, Predictive Analytics: Our machine learning service analyzes device metrics including CPU usage, memory usage, packet loss, latency, recent critical alerts, and interface errors to predict failure probability. It classifies risk levels as Low, Medium, High, or Critical and provides actionable recommendations. This enables proactive maintenance - addressing issues before they cause actual failures.

The system is built using modern technologies: Spring Boot for the backend, React for the frontend, MySQL for the database, and Python with scikit-learn for the ML service. It includes a simulation mode that allows testing and demonstration without real network infrastructure, which is what I showed you today.

The key innovation is the combination of alert correlation, root cause analysis, and predictive analytics in a single system. This significantly reduces Mean Time To Repair, decreases alert fatigue, and transforms network operations from reactive to proactive."

---

## Common Viva Questions and Answers

### Q1: How does the alert correlation algorithm work?

**Answer:** The correlation algorithm uses a multi-factor scoring approach. It considers:
1. Time proximity - alerts within a configurable time window (default 5 minutes)
2. Device topology - relationships between devices (router → switch → server)
3. Alert type patterns - predefined relationships like interface down → packet loss → timeout
4. Historical patterns - repeated alert combinations

Each factor has a weight, and the final correlation score is calculated as:
Score = (TimeScore × 0.4) + (DeviceScore × 0.25) + (AlertTypeScore × 0.25) + (HistoricalScore × 0.1)

If the score exceeds the threshold (default 60), alerts are grouped into an incident.

### Q2: What machine learning algorithm did you use for predictions?

**Answer:** I used a Random Forest Classifier from scikit-learn. Random Forest was chosen because:
- It handles non-linear relationships well
- It provides feature importance, making the model explainable
- It's robust to outliers
- It performs well with tabular data

The model is trained on synthetic data generated based on realistic failure patterns. If the ML service is unavailable, the system falls back to a rule-based scoring algorithm.

### Q3: How is the root cause confidence score calculated?

**Answer:** The root cause confidence score considers:
1. Temporal position (40% weight) - earliest alerts receive higher scores
2. Severity (25% weight) - critical alerts score higher
3. Device topology (25% weight) - routers score higher as they affect downstream devices
4. Alert type (10% weight) - interface down and routing errors score highest

The score is normalized to 0-100, and the alert with the highest score is identified as the probable root cause.

### Q4: What technologies did you use and why?

**Answer:**
- **Spring Boot**: For rapid backend development with built-in features like dependency injection, REST APIs, and database access
- **React**: For building a modern, responsive UI with component-based architecture
- **MySQL**: Reliable relational database with good performance for transactional data
- **Tailwind CSS**: For rapid, consistent styling without writing custom CSS
- **Python/scikit-learn**: For machine learning due to excellent ML libraries and ease of use
- **Recharts**: For data visualization in React

### Q5: How does the system handle real-time updates?

**Answer:** Currently, the frontend polls the backend every 10 seconds for dashboard updates. This provides near real-time updates without the complexity of WebSocket implementation. For production, WebSocket support can be added for true real-time updates.

### Q6: What is the fallback mechanism if the ML service is down?

**Answer:** The backend includes a rule-based prediction engine that activates if the ML service is unavailable. It uses a scoring algorithm:
- CPU > 80%: +20 points
- Memory > 80%: +15 points
- Packet Loss > 5%: +15 points
- Latency > 100ms: +10 points
- Recent Critical Alerts: +10 points each

If the total score >= 50, the device is classified as HIGH or CRITICAL risk. This ensures the system remains functional even without the ML service.

### Q7: How scalable is this system?

**Answer:** The system is designed with scalability in mind:
- Stateless REST APIs allow horizontal scaling
- Database connection pooling handles multiple connections
- Correlation and RCA can be parallelized
- ML service can be scaled independently
- For production, a message queue (Kafka/RabbitMQ) can be added for async processing

### Q8: What are the limitations of the current implementation?

**Answer:**
- Simulation mode uses synthetic data, not real network devices
- ML model is trained on synthetic data, not real historical data
- No authentication/authorization implemented
- WebSocket support not fully implemented (uses polling)
- Limited topology visualization
- Single-tenant design (no multi-organization support)

### Q9: How can this system be integrated with existing NMS tools?

**Answer:** The system provides REST APIs that can be integrated with existing tools:
- Alert ingestion via POST /api/alerts
- Device metrics via POST /api/devices
- Webhook notifications for new incidents
- Export/import of data via API
- For deeper integration, custom adapters can be built

### Q10: What is the impact of this system on network operations?

**Answer:**
- **Reduces Alert Fatigue**: Groups hundreds of alerts into manageable incidents
- **Decreases MTTR**: Faster root cause identification reduces repair time
- **Enables Proactive Maintenance**: Predictions allow addressing issues before failures
- **Improves Efficiency**: Operators focus on strategic tasks instead of repetitive troubleshooting
- **Enhances Visibility**: Comprehensive dashboard provides complete network overview

---

## Troubleshooting Common Issues

### Backend won't start

**Problem:** Port 8080 already in use

**Solution:**
```bash
# Windows: Find and kill process using port 8080
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Or change port in application.properties:
server.port=8081
```

### Database connection failed

**Problem:** MySQL credentials incorrect or service not running

**Solution:**
1. Verify MySQL is running: `mysql --version`
2. Check credentials in application.properties
3. Verify database exists: `SHOW DATABASES;`
4. Test connection: `mysql -u root -p netguard_ai`

### Frontend won't start

**Problem:** Port 5173 already in use or npm install failed

**Solution:**
```bash
# Kill process on port 5173
netstat -ano | findstr :5173
taskkill /PID <PID> /F

# Reinstall dependencies
rm -rf node_modules
npm install
```

### ML service won't start

**Problem:** Python dependencies not installed or port 5000 in use

**Solution:**
```bash
# Install dependencies
pip install -r requirements.txt

# Train model first
python train_model.py

# Start service
python app.py
```

**Note:** ML service is optional. System works with fallback predictions.

### CORS errors

**Problem:** Frontend cannot connect to backend

**Solution:**
1. Verify CORS configuration in CorsConfig.java
2. Check that backend is running on port 8080
3. Verify proxy configuration in vite.config.js

### No data in dashboard

**Problem:** Database not initialized

**Solution:**
1. Check backend logs for data initialization
2. Verify DataInitializer ran successfully
3. Manually insert sample data if needed

---

## Quick Reference: API Endpoints

### Simulation (for demo)
- `POST /api/simulation/router-failure` - Simulate router failure
- `POST /api/simulation/packet-loss` - Simulate packet loss
- `POST /api/simulation/high-cpu` - Simulate high CPU
- `POST /api/simulation/random-alerts?count=5` - Generate random alerts

### Analysis
- `POST /api/correlation/run` - Run alert correlation
- `GET /api/root-cause/incident/{id}` - Analyze root cause
- `POST /api/predictions/run` - Run predictions

### Data
- `GET /api/dashboard/summary` - Dashboard summary
- `GET /api/devices` - All devices
- `GET /api/alerts` - All alerts
- `GET /api/incidents` - All incidents
- `GET /api/predictions` - All predictions

---

## Demo Checklist

Before the demo, ensure:

- [ ] All services are running (Backend on 8080, Frontend on 5173)
- [ ] Dashboard loads with sample data
- [ ] MySQL database is accessible
- [ ] "Simulate Router Failure" button works
- [ ] Alert correlation creates incidents
- [ ] Root cause analysis returns results
- [ ] Predictions page displays data
- [ ] All pages navigate correctly
- [ ] No console errors in browser
- [ ] Backend logs show no errors

---

## IF YOU HAVE ONLY 30 MINUTES

### Minimum Setup for Quick Demo

1. **Install MySQL only** (5 minutes)
   - Download and install MySQL 8
   - Set root password to "root"
   - Start MySQL service

2. **Start Backend only** (10 minutes)
   - Ensure Java 17 is installed
   - Navigate to `backend` directory
   - Edit `application.properties` with MySQL password
   - Run: `mvn spring-boot:run`
   - Wait for startup and data initialization

3. **Start Frontend only** (10 minutes)
   - Ensure Node.js is installed
   - Navigate to `frontend` directory
   - Run: `npm install`
   - Run: `npm run dev`

4. **Open Dashboard** (5 minutes)
   - Navigate to http://localhost:5173
   - Click "Simulate Router Failure"
   - Navigate to Incidents page
   - Analyze root cause

**Note:** Skip ML service - the fallback prediction engine works perfectly for demo purposes.

---

## Additional Tips for Successful Demo

1. **Practice the demo sequence** at least once before the actual presentation
2. **Have backup screenshots** in case something fails during demo
3. **Know the hotkeys** for browser refresh (F5) and navigation
4. **Prepare for questions** by reviewing the Viva Q&A section
5. **Keep the system running** during the entire presentation
6. **Use a stable internet connection** (even though system runs locally)
7. **Have the code ready** to show if asked about implementation
8. **Be confident** in explaining the AI/ML components
9. **Focus on the business value** - reduced MTTR, proactive maintenance
10. **Show enthusiasm** for the project and its potential

---

**Good luck with your presentation!**
