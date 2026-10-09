import pandas as pd
import numpy as np
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report, accuracy_score
import joblib
import os

def generate_synthetic_data(n_samples=1000):
    """Generate synthetic training data for device failure prediction"""
    np.random.seed(42)

    data = []

    for _ in range(n_samples):
        cpu_usage = np.random.uniform(10, 100)
        memory_usage = np.random.uniform(10, 100)
        packet_loss = np.random.uniform(0, 15)
        latency = np.random.uniform(1, 300)
        recent_critical_alerts = np.random.randint(0, 10)
        interface_errors = np.random.randint(0, 200)

        # Determine if device will fail based on features
        risk_score = 0

        if cpu_usage > 85:
            risk_score += 25
        elif cpu_usage > 70:
            risk_score += 10

        if memory_usage > 85:
            risk_score += 20
        elif memory_usage > 70:
            risk_score += 8

        if packet_loss > 8:
            risk_score += 25
        elif packet_loss > 5:
            risk_score += 15
        elif packet_loss > 2:
            risk_score += 5

        if latency > 150:
            risk_score += 15
        elif latency > 100:
            risk_score += 8

        risk_score += recent_critical_alerts * 8

        if interface_errors > 100:
            risk_score += 10
        elif interface_errors > 50:
            risk_score += 5

        # Label: 1 = failure likely, 0 = stable
        failure_label = 1 if risk_score >= 50 else 0

        data.append({
            'cpu_usage': cpu_usage,
            'memory_usage': memory_usage,
            'packet_loss': packet_loss,
            'latency': latency,
            'recent_critical_alerts': recent_critical_alerts,
            'interface_errors': interface_errors,
            'failure': failure_label
        })

    return pd.DataFrame(data)

def train_model():
    """Train the Random Forest classifier and save the model"""
    print("Generating synthetic training data...")
    df = generate_synthetic_data(2000)

    print(f"Generated {len(df)} training samples")
    print(f"Failure cases: {df['failure'].sum()}")
    print(f"Stable cases: {len(df) - df['failure'].sum()}")

    # Prepare features and target
    X = df[['cpu_usage', 'memory_usage', 'packet_loss', 'latency',
            'recent_critical_alerts', 'interface_errors']]
    y = df['failure']

    # Split data
    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, random_state=42, stratify=y
    )

    # Train Random Forest
    print("Training Random Forest classifier...")
    model = RandomForestClassifier(
        n_estimators=100,
        max_depth=10,
        random_state=42,
        n_jobs=-1
    )

    model.fit(X_train, y_train)

    # Evaluate
    y_pred = model.predict(X_test)
    accuracy = accuracy_score(y_test, y_pred)

    print(f"Model accuracy: {accuracy:.4f}")
    print("\nClassification Report:")
    print(classification_report(y_test, y_pred))

    # Create model directory if it doesn't exist
    os.makedirs('model', exist_ok=True)

    # Save model
    model_path = 'model/failure_prediction_model.pkl'
    joblib.dump(model, model_path)
    print(f"\nModel saved to {model_path}")

    # Print feature importance
    feature_importance = pd.DataFrame({
        'feature': X.columns,
        'importance': model.feature_importances_
    }).sort_values('importance', ascending=False)

    print("\nFeature Importance:")
    print(feature_importance)

    return model

if __name__ == "__main__":
    train_model()
    print("\nTraining completed successfully!")
    print("You can now run the ML service with: python app.py")
