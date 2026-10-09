import { useState, useEffect } from 'react';
import { incidentAPI, rootCauseAPI } from '../services/api';
import { FileText, Search, CheckCircle, Clock } from 'lucide-react';

const Incidents = () => {
  const [incidents, setIncidents] = useState([]);
  const [selectedIncident, setSelectedIncident] = useState(null);
  const [rootCause, setRootCause] = useState(null);
  const [loading, setLoading] = useState(true);
  const [analyzing, setAnalyzing] = useState(false);

  useEffect(() => {
    loadIncidents();
  }, []);

  const loadIncidents = async () => {
    try {
      const response = await incidentAPI.getAll();
      setIncidents(response.data);
    } catch (error) {
      console.error('Error loading incidents:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAnalyzeRootCause = async (incidentId) => {
    setAnalyzing(true);
    try {
      const response = await rootCauseAPI.analyze(incidentId);
      setRootCause(response.data);
    } catch (error) {
      console.error('Root cause analysis error:', error);
    } finally {
      setAnalyzing(false);
    }
  };

  const handleResolveIncident = async (incidentId) => {
    try {
      await incidentAPI.resolve(incidentId);
      await loadIncidents();
    } catch (error) {
      console.error('Error resolving incident:', error);
    }
  };

  const getSeverityColor = (severity) => {
    switch (severity) {
      case 'CRITICAL': return 'bg-danger-500';
      case 'ERROR': return 'bg-danger-600';
      case 'WARNING': return 'bg-warning-500';
      case 'MEDIUM': return 'bg-primary-500';
      case 'LOW': return 'bg-accent-500';
      default: return 'bg-gray-500';
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case 'RESOLVED': return <CheckCircle className="text-accent-500" size={20} />;
      case 'INVESTIGATING': return <Search className="text-warning-500" size={20} />;
      default: return <Clock className="text-primary-500" size={20} />;
    }
  };

  if (loading) {
    return <div className="text-center text-gray-400">Loading incidents...</div>;
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-white">Incidents</h1>
        <p className="text-gray-400 mt-1">Correlated alert groups and root cause analysis</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="space-y-4">
          {incidents.map((incident) => (
            <div
              key={incident.id}
              className={`card cursor-pointer transition-all ${
                selectedIncident?.id === incident.id ? 'border-primary-500 border-2' : ''
              }`}
              onClick={() => {
                setSelectedIncident(incident);
                setRootCause(null);
              }}
            >
              <div className="flex items-start justify-between mb-3">
                <div className="flex items-center gap-3">
                  <FileText className="text-primary-500" size={24} />
                  <div>
                    <h3 className="text-lg font-bold text-white">{incident.title}</h3>
                    <p className="text-gray-400 text-sm">Incident #{incident.id}</p>
                  </div>
                </div>
                <div className="flex items-center gap-2">
                  {getStatusIcon(incident.status)}
                  <span className="text-white font-medium">{incident.status}</span>
                </div>
              </div>

              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <span className="text-gray-400 text-sm">Severity</span>
                  <span className={`px-2 py-1 rounded text-xs font-bold text-white ${getSeverityColor(incident.severity)}`}>
                    {incident.severity}
                  </span>
                </div>
                {incident.confidenceScore && (
                  <div className="flex items-center justify-between">
                    <span className="text-gray-400 text-sm">Confidence</span>
                    <span className="text-accent-500 font-bold">{incident.confidenceScore.toFixed(1)}%</span>
                  </div>
                )}
                <div className="flex items-center justify-between">
                  <span className="text-gray-400 text-sm">Created</span>
                  <span className="text-gray-300 text-sm">
                    {new Date(incident.createdAt).toLocaleString()}
                  </span>
                </div>
              </div>

              {incident.status !== 'RESOLVED' && (
                <button
                  onClick={(e) => {
                    e.stopPropagation();
                    handleResolveIncident(incident.id);
                  }}
                  className="btn-primary w-full mt-4"
                >
                  Resolve Incident
                </button>
              )}
            </div>
          ))}
        </div>

        {selectedIncident && (
          <div className="card">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-xl font-bold text-white">Incident Details</h2>
              <button
                onClick={() => handleAnalyzeRootCause(selectedIncident.id)}
                disabled={analyzing}
                className="btn-primary flex items-center gap-2"
              >
                <Search size={18} />
                {analyzing ? 'Analyzing...' : 'Analyze Root Cause'}
              </button>
            </div>

            <div className="space-y-4">
              <div>
                <h3 className="text-lg font-bold text-white mb-2">{selectedIncident.title}</h3>
                <p className="text-gray-400">ID: #{selectedIncident.id}</p>
              </div>

              {selectedIncident.rootCause && (
                <div className="bg-primary-200 p-4 rounded">
                  <h4 className="text-accent-500 font-bold mb-2">Probable Root Cause</h4>
                  <p className="text-white">{selectedIncident.rootCause}</p>
                  {selectedIncident.rootCauseDevice && (
                    <p className="text-gray-400 mt-1">Device: {selectedIncident.rootCauseDevice}</p>
                  )}
                </div>
              )}

              {rootCause && (
                <div className="bg-primary-200 p-4 rounded">
                  <h4 className="text-accent-500 font-bold mb-2">AI Analysis</h4>
                  <div className="space-y-2">
                    <div className="flex items-center justify-between">
                      <span className="text-gray-400">Root Cause Device</span>
                      <span className="text-white font-bold">{rootCause.rootCauseDevice}</span>
                    </div>
                    <div className="flex items-center justify-between">
                      <span className="text-gray-400">Root Cause Type</span>
                      <span className="text-white">{rootCause.rootCauseType}</span>
                    </div>
                    <div className="flex items-center justify-between">
                      <span className="text-gray-400">Confidence Score</span>
                      <span className="text-accent-500 font-bold">{rootCause.confidenceScore.toFixed(1)}%</span>
                    </div>
                    <div className="mt-3 pt-3 border-t border-primary-300">
                      <p className="text-gray-400 text-sm mb-2">Explanation:</p>
                      <p className="text-white text-sm whitespace-pre-line">{rootCause.explanation}</p>
                    </div>
                  </div>
                </div>
              )}

              {selectedIncident.affectedDevices && (
                <div>
                  <h4 className="text-gray-400 font-medium mb-2">Affected Devices</h4>
                  <p className="text-white">{selectedIncident.affectedDevices}</p>
                </div>
              )}

              {selectedIncident.aiRecommendation && (
                <div className="bg-primary-200 p-4 rounded">
                  <h4 className="text-primary-500 font-bold mb-2">AI Recommendation</h4>
                  <p className="text-white">{selectedIncident.aiRecommendation}</p>
                </div>
              )}

              <div className="grid grid-cols-2 gap-4 text-sm">
                <div>
                  <span className="text-gray-400">Created:</span>
                  <p className="text-white">{new Date(selectedIncident.createdAt).toLocaleString()}</p>
                </div>
                {selectedIncident.resolvedAt && (
                  <div>
                    <span className="text-gray-400">Resolved:</span>
                    <p className="text-white">{new Date(selectedIncident.resolvedAt).toLocaleString()}</p>
                  </div>
                )}
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Incidents;
