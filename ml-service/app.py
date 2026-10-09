from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import joblib
import numpy as np
import os

app = FastAPI(title="NetGuard AI ML Service", version="1.0.0")

MODEL_PATH = "model/failure_prediction_model.pkl"

class PredictionFeatures(BaseModel):
    cpu_usage: float
    memory_usage: float
    packet_loss: float
    latency: float
    recent_critical_alerts: int
    interface_errors: int = 0

class PredictionResponse(BaseModel):
    failure_probability: float
    risk_level: str
    predicted_failure: str
    confidence: float
    recommendation: str

def load_model():
    """Load the trained model or return None if not available"""
    if os.path.exists(MODEL_PATH):
        try:
            return joblib.load(MODEL_PATH)
        except Exception as e:
            print(f"Error loading model: {e}")
            return None
    return None

def calculate_fallback_prediction(features: PredictionFeatures) -> PredictionResponse:
    """Fallback prediction using rule-based logic when ML model is unavailable"""
    risk_score = 0.0

    # CPU usage contribution
    if features.cpu_usage > 90:
        risk_score += 30
    elif features.cpu_usage > 80:
        risk_score += 20
    elif features.cpu_usage > 70:
        risk_score += 10

    # Memory usage contribution
    if features.memory_usage > 90:
        risk_score += 25
    elif features.memory_usage > 80:
        risk_score += 15
    elif features.memory_usage > 70:
        risk_score += 8

    # Packet loss contribution
    if features.packet_loss > 10:
        risk_score += 25
    elif features.packet_loss > 5:
        risk_score += 15
    elif features.packet_loss > 2:
        risk_score += 8

    # Latency contribution
    if features.latency > 200:
        risk_score += 15
    elif features.latency > 100:
        risk_score += 10
    elif features.latency > 50:
        risk_score += 5

    # Recent critical alerts contribution
    risk_score += features.recent_critical_alerts * 5

    # Interface errors contribution
    if features.interface_errors > 100:
        risk_score += 10
    elif features.interface_errors > 50:
        risk_score += 5

    risk_score = min(100.0, risk_score)
    failure_probability = risk_score / 100.0

    # Determine risk level
    if risk_score >= 75:
        risk_level = "CRITICAL"
        predicted_failure = "High probability of device failure"
        recommendation = "Immediate investigation required. Check system resources, logs, and interface status."
    elif risk_score >= 50:
        risk_level = "HIGH"
        predicted_failure = "Potential performance degradation"
        recommendation = "Monitor closely. Consider preventive maintenance and resource optimization."
    elif risk_score >= 25:
        risk_level = "MEDIUM"
        predicted_failure = "Possible minor issues"
        recommendation = "Regular monitoring recommended. Investigate if conditions persist."
    else:
        risk_level = "LOW"
        predicted_failure = "Stable operation expected"
        recommendation = "Continue normal monitoring. No immediate action required."

    return PredictionResponse(
        failure_probability=failure_probability,
        risk_level=risk_level,
        predicted_failure=predicted_failure,
        confidence=0.75,
        recommendation=recommendation
    )

@app.get("/")
def read_root():
    return {
        "service": "NetGuard AI ML Service",
        "status": "running",
        "endpoints": {
            "/predict": "POST - Predict device failure risk",
            "/health": "GET - Check service health"
        }
    }

@app.get("/health")
def health_check():
    model = load_model()
    return {
        "status": "healthy",
        "model_loaded": model is not None
    }

@app.post("/predict", response_model=PredictionResponse)
def predict(features: PredictionFeatures):
    """Predict device failure probability based on input features"""
    model = load_model()

    if model is not None:
        try:
            # Prepare features for model
            feature_array = np.array([[
                features.cpu_usage,
                features.memory_usage,
                features.packet_loss,
                features.latency,
                features.recent_critical_alerts,
                features.interface_errors
            ]])

            # Get prediction probability
            probability = model.predict_proba(feature_array)[0][1]

            # Determine risk level and recommendations
            if probability >= 0.75:
                risk_level = "CRITICAL"
                predicted_failure = "High probability of device failure"
                recommendation = "Immediate investigation required. Check system resources, logs, and interface status."
            elif probability >= 0.50:
                risk_level = "HIGH"
                predicted_failure = "Potential performance degradation"
                recommendation = "Monitor closely. Consider preventive maintenance and resource optimization."
            elif probability >= 0.25:
                risk_level = "MEDIUM"
                predicted_failure = "Possible minor issues"
                recommendation = "Regular monitoring recommended. Investigate if conditions persist."
            else:
                risk_level = "LOW"
                predicted_failure = "Stable operation expected"
                recommendation = "Continue normal monitoring. No immediate action required."

            return PredictionResponse(
                failure_probability=float(probability),
                risk_level=risk_level,
                predicted_failure=predicted_failure,
                confidence=0.85,
                recommendation=recommendation
            )
        except Exception as e:
            print(f"ML prediction error: {e}")
            # Fallback to rule-based if model prediction fails
            return calculate_fallback_prediction(features)
    else:
        # Use fallback prediction if model is not available
        return calculate_fallback_prediction(features)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=5000)
