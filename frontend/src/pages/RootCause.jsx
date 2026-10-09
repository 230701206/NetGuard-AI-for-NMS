import { useState } from 'react';
import { incidentAPI, rootCauseAPI } from '../services/api';
import { Search, FileText, AlertCircle } from 'lucide-react';

const RootCause = () => {
  const [incidentId, setIncidentId] = useState('');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleAnalyze = async (e) => {
    e.preventDefault();
    if (!incidentId) return;

    setLoading(true);
    setError(null);
    setResult(null);

    try {
      const response = await rootCauseAPI.analyze(incidentId);
      setResult(response.data);
    } catch (err) {
      setError(err.response?.data?.error || 'Analysis failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-white">Root Cause Analysis</h1>
        <p className="text-gray-400 mt-1">AI-powered root cause identification for incidents</p>
      </div>

      <div className="card">
        <form onSubmit={handleAnalyze} className="flex gap-4">
          <input
            type="number"
            value={incidentId}
            onChange={(e) => setIncidentId(e.target.value)}
            placeholder="Enter Incident ID"
            className="flex-1 bg-primary-200 border border-primary-300 rounded px-4 py-2 text-white placeholder-gray-500 focus:outline-none focus:border-primary-500"
          />
          <button
            type="submit"
            disabled={loading || !incidentId}
            className="btn-primary flex items-center gap-2"
          >
            <Search size={20} />
            {loading ? 'Analyzing...' : 'Analyze'}
          </button>
        </form>
      </div>

      {error && (
        <div className="card border-danger-500">
          <div className="flex items-center gap-3 text-danger-500">
            <AlertCircle size={24} />
            <p className="font-medium">{error}</p>
          </div>
        </div>
      )}

      {result && (
        <div className="card">
          <div className="flex items-center gap-3 mb-6">
            <FileText className="text-primary-500" size={32} />
            <h2 className="text-2xl font-bold text-white">Analysis Results</h2>
          </div>

          <div className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="bg-primary-200 p-4 rounded">
                <h3 className="text-gray-400 text-sm mb-1">Root Cause Device</h3>
                <p className="text-xl font-bold text-white">{result.rootCauseDevice}</p>
              </div>

              <div className="bg-primary-200 p-4 rounded">
                <h3 className="text-gray-400 text-sm mb-1">Root Cause Type</h3>
                <p className="text-xl font-bold text-white">{result.rootCauseType}</p>
              </div>

              <div className="bg-primary-200 p-4 rounded">
                <h3 className="text-gray-400 text-sm mb-1">Confidence Score</h3>
                <p className="text-3xl font-bold text-accent-500">{result.confidenceScore.toFixed(1)}%</p>
              </div>

              <div className="bg-primary-200 p-4 rounded">
                <h3 className="text-gray-400 text-sm mb-1">Analysis Time</h3>
                <p className="text-white">{new Date(result.analysisTimestamp).toLocaleString()}</p>
              </div>
            </div>

            <div className="bg-primary-200 p-6 rounded">
              <h3 className="text-lg font-bold text-white mb-3">Explanation</h3>
              <p className="text-gray-300 whitespace-pre-line leading-relaxed">
                {result.explanation}
              </p>
            </div>

            <div className="bg-accent-500/10 border border-accent-500 p-4 rounded">
              <h3 className="text-accent-500 font-bold mb-2">How This Works</h3>
              <ul className="text-gray-300 space-y-2 text-sm">
                <li>• Analyzes temporal proximity of alerts</li>
                <li>• Evaluates device topology relationships</li>
                <li>• Considers alert severity and type patterns</li>
                <li>• Calculates confidence based on multiple factors</li>
                <li>• Identifies earliest critical events as potential root causes</li>
              </ul>
            </div>
          </div>
        </div>
      )}

      {!result && !error && (
        <div className="card text-center py-12">
          <Search className="text-gray-500 mx-auto mb-4" size={48} />
          <p className="text-gray-400">Enter an incident ID to analyze its root cause</p>
        </div>
      )}
    </div>
  );
};

export default RootCause;
