# NetGuard AI - ML Service

Python FastAPI service for device failure prediction using scikit-learn.

## Features

- REST API for failure prediction
- Random Forest Classifier model
- Rule-based fallback when model unavailable
- Feature importance analysis
- Risk level classification (LOW, MEDIUM, HIGH, CRITICAL)

## Installation

1. Install Python 3.8 or higher

2. Install dependencies:
```bash
pip install -r requirements.txt
```

3. Train the model:
```bash
python train_model.py
```

This will:
- Generate synthetic training data (2000 samples)
- Train a Random Forest classifier
- Save the model to `model/failure_prediction_model.pkl`
- Display model accuracy and feature importance

4. Start the service:
```bash
python app.py
```

The service will start on http://localhost:5000

## API Endpoints

### GET /
Service information

### GET /health
Health check - returns whether model is loaded

### POST /predict
Predict device failure risk

**Request Body:**
```json
{
  "cpu_usage": 85.0,
  "memory_usage": 72.0,
  "packet_loss": 3.5,
  "latency": 45.0,
  "recent_critical_alerts": 2,
  "interface_errors": 50
}
```

**Response:**
```json
{
  "failure_probability": 0.68,
  "risk_level": "HIGH",
  "predicted_failure": "Potential performance degradation",
  "confidence": 0.85,
  "recommendation": "Monitor closely. Consider preventive maintenance and resource optimization."
}
```

## Features Used

1. **cpu_usage**: Current CPU utilization percentage (0-100)
2. **memory_usage**: Current memory utilization percentage (0-100)
3. **packet_loss**: Network packet loss percentage (0-100)
4. **latency**: Network latency in milliseconds (0-1000)
5. **recent_critical_alerts**: Count of critical alerts in last 24 hours (0-10)
6. **interface_errors**: Number of interface errors (0-200)

## Risk Classification

- **LOW**: Failure probability < 25%
- **MEDIUM**: Failure probability 25-50%
- **HIGH**: Failure probability 50-75%
- **CRITICAL**: Failure probability > 75%

## Fallback Mechanism

If the ML model is not available or prediction fails, the service uses a rule-based scoring algorithm:

- CPU > 90%: +30 points
- CPU > 80%: +20 points
- Memory > 90%: +25 points
- Memory > 80%: +15 points
- Packet Loss > 10%: +25 points
- Packet Loss > 5%: +15 points
- Latency > 200ms: +15 points
- Latency > 100ms: +10 points
- Recent Critical Alerts: +5 points each
- Interface Errors > 100: +10 points

Score >= 50: HIGH/CRITICAL risk

## Model Details

- **Algorithm**: Random Forest Classifier
- **Estimators**: 100
- **Max Depth**: 10
- **Training Data**: Synthetic (2000 samples)
- **Features**: 6
- **Target**: Binary (failure/stable)

## Testing

Test the service using curl:

```bash
curl -X POST http://localhost:5000/predict \
  -H "Content-Type: application/json" \
  -d '{
    "cpu_usage": 85.0,
    "memory_usage": 72.0,
    "packet_loss": 3.5,
    "latency": 45.0,
    "recent_critical_alerts": 2,
    "interface_errors": 50
  }'
```

## Integration with Backend

The Spring Boot backend calls this service at:
```
http://localhost:5000/predict
```

If the service is unavailable, the backend uses its own fallback prediction engine.

## Development

To regenerate the model with different parameters:

1. Edit `train_model.py`
2. Modify parameters (n_estimators, max_depth, etc.)
3. Run: `python train_model.py`
4. Restart the service

## Notes

- The ML service is **optional** for the NetGuard AI system
- The backend includes a fallback prediction engine
- For production, train the model on real historical data
- Consider retraining the model periodically with new data
