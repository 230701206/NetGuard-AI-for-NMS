import { useState, useEffect } from 'react';
import { dashboardAPI, simulationAPI } from '../services/api';
import { Activity, Server, AlertTriangle, FileText, TrendingUp, Zap, RefreshCw } from 'lucide-react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';

const Dashboard = () => {
  const [summary, setSummary] = useState(null);
  const [recentAlerts, setRecentAlerts] = useState([]);
  const [recentIncidents, setRecentIncidents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [simulating, setSimulating] = useState(false);

  useEffect(() => {
    loadData();
    const interval = setInterval(loadData, 10000);
    return () => clearInterval(interval);
  }, []);

  const loadData = async () => {
    try {
      const [summaryRes, alertsRes, incidentsRes] = await Promise.all([
        dashboardAPI.getSummary(),
        dashboardAPI.getRecentAlerts(10),
        dashboardAPI.getRecentIncidents(5),
      ]);

      setSummary(summaryRes.data);
      setRecentAlerts(alertsRes.data);
      setRecentIncidents(incidentsRes.data);
    } catch (error) {
      console.error('Error loading dashboard data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSimulateRouterFailure = async () => {
    setSimulating(true);
    try {
      await simulationAPI.routerFailure();
      await loadData();
    } catch (error) {
      console.error('Simulation error:', error);
    } finally {
      setSimulating(false);
    }
  };

  const getSeverityColor = (severity) => {
    switch (severity) {
      case 'CRITICAL': return 'bg-danger-500';
      case 'ERROR': return 'bg-danger-600';
      case 'WARNING': return 'bg-warning-500';
      default: return 'bg-primary-500';
    }
  };

  const getRiskColor = (riskLevel) => {
    switch (riskLevel) {
      case 'CRITICAL': return 'text-danger-600';
      case 'HIGH': return 'text-danger-500';
      case 'MEDIUM': return 'text-warning-500';
      default: return 'text-accent-500';
    }
  };

  if (loading) {
    return <div className="flex items-center justify-center h-full">
      <RefreshCw className="animate-spin text-primary-500" size={40} />
    </div>;
  }

  const healthData = [
    { name: 'Healthy', value: summary?.activeDevices || 0 },
    { name: 'At Risk', value: summary?.highRiskDevices || 0 },
  ];

  const severityData = [
    { name: 'Critical', value: summary?.criticalAlerts || 0 },
    { name: 'Open Incidents', value: summary?.openIncidents || 0 },
  ];

  const COLORS = ['#22c55e', '#ef4444'];

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-white">Dashboard</h1>
          <p className="text-gray-400 mt-1">Network monitoring overview</p>
        </div>
        <button
          onClick={handleSimulateRouterFailure}
          disabled={simulating}
          className="btn-danger flex items-center gap-2"
        >
          <Zap size={20} />
          {simulating ? 'Simulating...' : 'Simulate Router Failure'}
        </button>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-6 gap-4">
        <div className="card">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-400 text-sm">Total Devices</p>
              <p className="text-3xl font-bold text-white mt-1">{summary?.totalDevices || 0}</p>
            </div>
            <Server className="text-primary-500" size={32} />
          </div>
        </div>

        <div className="card">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-400 text-sm">Active Devices</p>
              <p className="text-3xl font-bold text-accent-500 mt-1">{summary?.activeDevices || 0}</p>
            </div>
            <Activity className="text-accent-500" size={32} />
          </div>
        </div>

        <div className="card">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-400 text-sm">Critical Alerts</p>
              <p className="text-3xl font-bold text-danger-500 mt-1">{summary?.criticalAlerts || 0}</p>
            </div>
            <AlertTriangle className="text-danger-500" size={32} />
          </div>
        </div>

        <div className="card">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-400 text-sm">Open Incidents</p>
              <p className="text-3xl font-bold text-warning-500 mt-1">{summary?.openIncidents || 0}</p>
            </div>
            <FileText className="text-warning-500" size={32} />
          </div>
        </div>

        <div className="card">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-400 text-sm">High Risk Devices</p>
              <p className="text-3xl font-bold text-danger-600 mt-1">{summary?.highRiskDevices || 0}</p>
            </div>
            <TrendingUp className="text-danger-600" size={32} />
          </div>
        </div>

        <div className="card">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-400 text-sm">Network Health</p>
              <p className="text-3xl font-bold text-accent-500 mt-1">
                {summary?.networkHealthPercentage?.toFixed(1) || 0}%
              </p>
            </div>
            <Activity className="text-accent-500" size={32} />
          </div>
        </div>
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="card">
          <h2 className="text-xl font-bold text-white mb-4">Device Health Distribution</h2>
          <ResponsiveContainer width="100%" height={250}>
            <PieChart>
              <Pie
                data={healthData}
                cx="50%"
                cy="50%"
                labelLine={false}
                label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                outerRadius={80}
                fill="#8884d8"
                dataKey="value"
              >
                {healthData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </div>

        <div className="card">
          <h2 className="text-xl font-bold text-white mb-4">Alert Severity Distribution</h2>
          <ResponsiveContainer width="100%" height={250}>
            <BarChart data={severityData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
              <XAxis dataKey="name" stroke="#94a3b8" />
              <YAxis stroke="#94a3b8" />
              <Tooltip />
              <Bar dataKey="value" fill="#0ea5e9" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Recent Alerts and Incidents */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="card">
          <h2 className="text-xl font-bold text-white mb-4">Recent Alerts</h2>
          <div className="space-y-3">
            {recentAlerts.length === 0 ? (
              <p className="text-gray-400">No recent alerts</p>
            ) : (
              recentAlerts.map((alert) => (
                <div key={alert.id} className="flex items-center justify-between p-3 bg-primary-200 rounded">
                  <div className="flex items-center gap-3">
                    <span className={`px-2 py-1 rounded text-xs font-bold ${getSeverityColor(alert.severity)}`}>
                      {alert.severity}
                    </span>
                    <div>
                      <p className="text-white font-medium">{alert.deviceHostname}</p>
                      <p className="text-gray-400 text-sm">{alert.message}</p>
                    </div>
                  </div>
                  <span className="text-gray-500 text-sm">
                    {new Date(alert.timestamp).toLocaleTimeString()}
                  </span>
                </div>
              ))
            )}
          </div>
        </div>

        <div className="card">
          <h2 className="text-xl font-bold text-white mb-4">Recent Incidents</h2>
          <div className="space-y-3">
            {recentIncidents.length === 0 ? (
              <p className="text-gray-400">No recent incidents</p>
            ) : (
              recentIncidents.map((incident) => (
                <div key={incident.id} className="p-3 bg-primary-200 rounded">
                  <div className="flex items-center justify-between mb-2">
                    <span className={`px-2 py-1 rounded text-xs font-bold ${getSeverityColor(incident.severity)}`}>
                      {incident.severity}
                    </span>
                    <span className="text-gray-500 text-sm">
                      {new Date(incident.createdAt).toLocaleString()}
                    </span>
                  </div>
                  <p className="text-white font-medium">{incident.title}</p>
                  {incident.rootCause && (
                    <p className="text-gray-400 text-sm mt-1">
                      Root Cause: {incident.rootCause}
                    </p>
                  )}
                  {incident.confidenceScore && (
                    <p className="text-accent-500 text-sm mt-1">
                      Confidence: {incident.confidenceScore.toFixed(1)}%
                    </p>
                  )}
                </div>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
