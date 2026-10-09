# NetGuard AI — Emergency Run Guide

This version is configured to run **without the Python ML service**. The Spring Boot application contains a built-in explainable AI/risk-scoring engine for the demo. The Python Random Forest service remains optional in `ml-service/`.

## 1. Requirements

- Java 17 or 21
- Maven 3.8+
- MySQL 8
- Node.js 18+ and npm

## 2. MySQL

Start MySQL and create the database:

```sql
CREATE DATABASE IF NOT EXISTS netguard_ai;
```

Default credentials are:

- user: `root`
- password: `root`

If your MySQL password is different, either edit `backend/src/main/resources/application.properties` or set:

```cmd
set MYSQL_USER=root
set MYSQL_PASSWORD=YOUR_PASSWORD
```

## 3. Start backend

Open terminal 1:

```cmd
cd backend
mvn clean spring-boot:run
```

Wait for:

```text
Started NetGuardAiApplication
```

The backend runs at `http://localhost:8080`.

The application automatically creates sample devices, alerts, incidents, predictions and metrics in an empty database.

## 4. Start frontend

Open terminal 2:

```cmd
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`.

## 5. Demo

1. Open Dashboard.
2. Show the seven sample network devices.
3. Show alerts/incidents/predictions.
4. Click **Simulate Router Failure**.
5. The backend generates a cascade of network alerts.
6. Alert correlation groups related events.
7. Root-cause analysis identifies the likely router/interface failure.
8. A new incident is created with confidence and recommendation.
9. Device risk predictions are recalculated.

## 6. Optional ML service

You do NOT need this for the submission demo.

If time permits:

```cmd
cd ml-service
python -m venv venv
venv\Scripts\activate
pip install -r requirements.txt
python train_model.py
python app.py
```

Then change `ml.service.enabled=false` to `true` in `backend/src/main/resources/application.properties` and restart the backend.

If the Python service is unavailable, the backend automatically falls back to its built-in prediction engine.

## 7. Important

Do not delete the database while the app is running. Hibernate uses `ddl-auto=update` and the `DataInitializer` seeds data only when there are no devices.
