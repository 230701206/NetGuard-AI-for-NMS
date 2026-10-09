import { useState, useEffect } from 'react';
import { predictionAPI } from '../services/api';
import { TrendingUp, Brain, RefreshCw, AlertTriangle } from 'lucide-react';
import { PieChart, Pie, Cell, ResponsiveContainer, Tooltip, Legend } from 'recharts';

const Predictions = () => {
  const [predictions, setPredictions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [running, setRunning] = useState(false);

  useEffect(() => {
    loadPredictions();
  }, []);

  const loadPredictions = async () => {
    try {
      const response = await predictionAPI.getAll();
      setPredictions(response.data);
    } catch (error) {
      console.error('Error loading predictions:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleRunPredictions = async () => {
    setRunning(true);
    try {
      await predictionAPI.runAll();
      await loadPredictions();
    } catch (error) {
      console.error('Error running predictions:', error);
    } finally {
      setRunning(false);
    }
  };

  const getRiskColor = (riskLevel) => {
    switch (riskLevel) {
      case 'CRITICAL': return 'bg-danger-600';
      case 'HIGH': return 'bg-danger-500';
      case 'MEDIUM': return 'bg-warning-500';
      case 'LOW': return 'bg-accent-500';
      default: return 'bg-gray-500';
    }
  };

  const getRiskTextColor = (riskLevel) => {
    switch (riskLevel) {
      case 'CRITICAL': return 'text-danger-600';
      case 'HIGH': return 'text-danger-500';
      case 'MEDIUM': return 'text-warning-500';
      case 'LOW': return 'text-accent-500';
      default: return 'text-gray-400';
    }
  };

  if (loading) {
    return <div className="text-center text-gray-400">Loading predictions...</div>;
  }

  const riskDistribution = [
    { name: 'Critical', value: predictions.filter(p => p.riskLevel === 'CRITICAL').length },
    { name: 'High', value: predictions.filter(p => p.riskLevel === 'HIGH').length },
    { name: 'Medium', value: predictions.filter(p => p.riskLevel === 'MEDIUM').length },
    { name: 'Low', value: predictions.filter(p => p.riskLevel === 'LOW').length },
  ].filter(d => d.value > 0);

  const COLORS = ['#dc2626', '#ef4444', '#f59e0b', '#22c55e'];

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-white">AI Predictions</h1>
          <p className="text-gray-400 mt-1">Machine learning-based failure risk predictions</p>
        </div>
        <button
          onClick={handleRunPredictions}
          disabled={running}
          className="btn-primary flex items-center gap-2"
        >
          <Brain size={20} />
          {running ? 'Running...' : 'Run Predictions'}
        </button>
      </div>

      {riskDistribution.length > 0 && (
        <div className="card">
          <h2 className="text-xl font-bold text-white mb-4">Risk Distribution</h2>
          <ResponsiveContainer width="100%" height={250}>
            <PieChart>
              <Pie
                data={riskDistribution}
                cx="50%"
                cy="50%"
                labelLine={false}
                label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                outerRadius={80}
                fill="#8884d8"
                dataKey="value"
              >
                {riskDistribution.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip />
              <Legend />
            </PieChart>
          </ResponsiveContainer>
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {predictions.map((prediction) => (
          <div key={prediction.id} className="card">
            <div className="flex items-start justify-between mb-4">
              <div className="flex items-center gap-3">
                <Brain className="text-primary-500" size={28} />
                <div>
                  <h3 className="text-lg font-bold text-white">{prediction.deviceHostname}</h3>
                  <p className="text-gray-400 text-sm">Prediction #{prediction.id}</p>
                </div>
              </div>
              <span className={`px-3 py-1 rounded text-xs font-bold text-white ${getRiskColor(prediction.riskLevel)}`}>
                {prediction.riskLevel}
              </span>
            </div>

            <div className="space-y-3">
              <div className="flex items-center justify-between">
                <span className="text-gray-400 text-sm">Failure Probability</span>
                <span className={`text-2xl font-bold ${getRiskTextColor(prediction.riskLevel)}`}>
                  {(prediction.failureProbability * 100).toFixed(1)}%
                </span>
              </div>

              <div className="flex items-center justify-between">
                <span className="text-gray-400 text-sm">Confidence</span>
                <span className="text-white">{(prediction.confidence * 100).toFixed(1)}%</span>
              </div>

              <div className="border-t border-primary-300 pt-3">
                <div className="flex items-start gap-2 mb-2">
                  <AlertTriangle size={16} className="text-warning-500 mt-1" />
                  <div>
                    <p className="text-gray-400 text-sm">Predicted Failure</p>
                    <p className="text-white">{prediction.predictedFailure}</p>
                  </div>
                </div>
              </div>

              <div className="bg-primary-200 p-3 rounded">
                <p className="text-gray-400 text-sm mb-1">Recommendation</p>
                <p className="text-white text-sm">{prediction.recommendation}</p>
              </div>

              <div className="text-xs text-gray-500">
                Predicted: {new Date(prediction.timestamp).toLocaleString()}
              </div>
            </div>
          </div>
        ))}
      </div>

      {predictions.length === 0 && (
        <div className="card text-center py-12">
          <Brain className="text-gray-500 mx-auto mb-4" size={48} />
          <p className="text-gray-400">No predictions available. Click "Run Predictions" to generate predictions.</p>
        </div>
      )}
    </div>
  );
};

export default Predictions;
