# NetGuard AI - Quick Start Guide

## Fastest Way to Get Running (15-20 minutes)

### Prerequisites Check

Before starting, verify you have:
- [ ] Java 17+ installed (`java -version`)
- [ ] Maven installed (`mvn -version`)
- [ ] MySQL 8 installed and running
- [ ] Node.js 18+ installed (`node -version`)

If any are missing, install them first.

---

## STEP 1: Database Setup (5 minutes)

1. **Start MySQL Service**
   - Windows: Services → MySQL → Start
   - Or run: `net start MySQL80`

2. **Create Database**
   ```bash
   mysql -u root -p
   ```
   Enter your password, then:
   ```sql
   CREATE DATABASE netguard_ai;
   EXIT;
   ```

3. **Verify Database**
   ```bash
   mysql -u root -p -e "SHOW DATABASES;"
   ```
   You should see `netguard_ai` in the list.

---

## STEP 2: Backend Setup (5 minutes)

1. **Navigate to Backend Directory**
   ```bash
   cd netguard-ai/backend
   ```

2. **Configure Database Connection**
   - Open `src/main/resources/application.properties`
   - Update these lines:
     ```properties
     spring.datasource.username=root
     spring.datasource.password=YOUR_MYSQL_PASSWORD
     ```

3. **Build and Run Backend**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Wait for Startup**
   - Look for: `Started NetGuardAiApplication in X seconds`
   - Sample data will be automatically loaded
   - Backend runs on http://localhost:8080

**Don't close this terminal!** Keep it running.

---

## STEP 3: Frontend Setup (5 minutes)

1. **Open New Terminal** (keep backend terminal open)

2. **Navigate to Frontend Directory**
   ```bash
   cd netguard-ai/frontend
   ```

3. **Install Dependencies**
   ```bash
   npm install
   ```
   (This may take 2-3 minutes on first run)

4. **Start Frontend**
   ```bash
   npm run dev
   ```

5. **Wait for Startup**
   - Look for: `Local: http://localhost:5173/`
   - Frontend runs on http://localhost:5173

**Don't close this terminal!** Keep it running.

---

## STEP 4: Open Dashboard (1 minute)

1. **Open Browser**
   - Navigate to: http://localhost:5173

2. **Verify Dashboard Loads**
   - You should see the NetGuard AI dashboard
   - Pre-populated with sample data
   - Charts and metrics visible

---

## STEP 5: Test the System (2 minutes)

1. **Click "Simulate Router Failure"** button on Dashboard
2. **Watch the dashboard update** with new alerts
3. **Navigate to Incidents page** (sidebar)
4. **Click on the new incident**
5. **Click "Analyze Root Cause"** button
6. **View the AI analysis results**

---

## OPTIONAL: ML Service (5 minutes)

The system works without ML service (uses fallback predictions). To use the actual ML service:

1. **Open New Terminal**

2. **Navigate to ML Service Directory**
   ```bash
   cd netguard-ai/ml-service
   ```

3. **Install Python Dependencies**
   ```bash
   pip install -r requirements.txt
   ```

4. **Train the Model**
   ```bash
   python train_model.py
   ```

5. **Start ML Service**
   ```bash
   python app.py
   ```

6. **Verify ML Service**
   - Navigate to: http://localhost:5000
   - You should see API documentation

---

## Common Issues and Solutions

### Issue: Port 8080 already in use

**Solution:**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

Or change port in `application.properties`:
```properties
server.port=8081
```

### Issue: Port 5173 already in use

**Solution:**
```bash
# Windows
netstat -ano | findstr :5173
taskkill /PID <PID> /F
```

### Issue: MySQL connection failed

**Solution:**
1. Verify MySQL is running: `mysql --version`
2. Check password in `application.properties`
3. Test connection: `mysql -u root -p netguard_ai`

### Issue: npm install fails

**Solution:**
```bash
# Clear cache and retry
npm cache clean --force
npm install
```

### Issue: Backend won't start

**Solution:**
1. Check Java version: `java -version` (must be 17+)
2. Check Maven version: `mvn -version`
3. Delete `target` folder and retry: `mvn clean install`

### Issue: No data in dashboard

**Solution:**
1. Check backend logs for data initialization
2. Verify database has tables: `mysql -u root -p netguard_ai -e "SHOW TABLES;"`
3. Restart backend to trigger data initialization

---

## Verification Checklist

Before your demo, verify:

- [ ] Backend terminal shows "Started NetGuardAiApplication"
- [ ] Frontend terminal shows "Local: http://localhost:5173/"
- [ ] Browser shows dashboard at http://localhost:5173
- [ ] Dashboard has sample data (devices, alerts, incidents)
- [ ] "Simulate Router Failure" button works
- [ ] Incidents page shows data
- [ ] Root cause analysis returns results
- [ ] Predictions page shows data

---

## Stopping the System

To stop all services:

1. **Frontend**: In frontend terminal, press `Ctrl+C`
2. **Backend**: In backend terminal, press `Ctrl+C`
3. **ML Service** (if running): In ML terminal, press `Ctrl+C`
4. **MySQL**: Stop if desired (not required)

---

## Next Steps

After successful setup:

1. Read the full [README.md](README.md) for detailed documentation
2. Read [demo-guide.md](demo-guide.md) for presentation instructions
3. Practice the demo sequence
4. Prepare for viva questions

---

## Need Help?

- Check [demo-guide.md](demo-guide.md) for troubleshooting
- Review [README.md](README.md) for technical details
- Verify all prerequisites are installed correctly

---

**System Requirements:**
- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Node.js 18+
- Python 3.8+ (optional, for ML service)

**System Runs On:**
- Windows 10/11
- macOS
- Linux

**Ports Used:**
- Backend: 8080
- Frontend: 5173
- ML Service: 5000 (optional)
- MySQL: 3306 (default)
