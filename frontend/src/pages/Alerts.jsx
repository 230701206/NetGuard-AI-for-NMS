import { useState, useEffect } from 'react';
import { alertAPI, correlationAPI } from '../services/api';
import { AlertTriangle, RefreshCw, Link2 } from 'lucide-react';

const Alerts = () => {
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [correlating, setCorrelating] = useState(false);

  useEffect(() => {
    loadAlerts();
  }, []);

  const loadAlerts = async () => {
    try {
      const response = await alertAPI.getAll();
      setAlerts(response.data);
    } catch (error) {
      console.error('Error loading alerts:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleRunCorrelation = async () => {
    setCorrelating(true);
    try {
      await correlationAPI.run();
      await loadAlerts();
    } catch (error) {
      console.error('Correlation error:', error);
    } finally {
      setCorrelating(false);
    }
  };

  const getSeverityColor = (severity) => {
    switch (severity) {
      case 'CRITICAL': return 'bg-danger-500';
      case 'ERROR': return 'bg-danger-600';
      case 'WARNING': return 'bg-warning-500';
      case 'INFO': return 'bg-primary-500';
      default: return 'bg-gray-500';
    }
  };

  if (loading) {
    return <div className="text-center text-gray-400">Loading alerts...</div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-white">Alerts</h1>
          <p className="text-gray-400 mt-1">Network alerts and notifications</p>
        </div>
        <button
          onClick={handleRunCorrelation}
          disabled={correlating}
          className="btn-primary flex items-center gap-2"
        >
          <Link2 size={20} />
          {correlating ? 'Correlating...' : 'Run Correlation'}
        </button>
      </div>

      <div className="card">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-primary-300">
                <th className="text-left py-3 px-4 text-gray-400 font-medium">Severity</th>
                <th className="text-left py-3 px-4 text-gray-400 font-medium">Device</th>
                <th className="text-left py-3 px-4 text-gray-400 font-medium">Type</th>
                <th className="text-left py-3 px-4 text-gray-400 font-medium">Message</th>
                <th className="text-left py-3 px-4 text-gray-400 font-medium">Timestamp</th>
                <th className="text-left py-3 px-4 text-gray-400 font-medium">Status</th>
                <th className="text-left py-3 px-4 text-gray-400 font-medium">Correlated</th>
              </tr>
            </thead>
            <tbody>
              {alerts.length === 0 ? (
                <tr>
                  <td colSpan="7" className="text-center py-8 text-gray-400">No alerts found</td>
                </tr>
              ) : (
                alerts.map((alert) => (
                  <tr key={alert.id} className="border-b border-primary-300 hover:bg-primary-200">
                    <td className="py-3 px-4">
                      <span className={`px-2 py-1 rounded text-xs font-bold text-white ${getSeverityColor(alert.severity)}`}>
                        {alert.severity}
                      </span>
                    </td>
                    <td className="py-3 px-4 text-white">{alert.deviceHostname}</td>
                    <td className="py-3 px-4 text-gray-300">{alert.alertType}</td>
                    <td className="py-3 px-4 text-gray-300">{alert.message}</td>
                    <td className="py-3 px-4 text-gray-400 text-sm">
                      {new Date(alert.timestamp).toLocaleString()}
                    </td>
                    <td className="py-3 px-4">
                      <span className="text-white">{alert.status}</span>
                    </td>
                    <td className="py-3 px-4">
                      {alert.correlated ? (
                        <span className="text-accent-500">Yes</span>
                      ) : (
                        <span className="text-gray-500">No</span>
                      )}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default Alerts;
