import { useState, useEffect } from 'react';
import { dashboardAPI, deviceAPI } from '../services/api';
import { Heart, Activity, Server, TrendingUp, TrendingDown } from 'lucide-react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, AreaChart, Area } from 'recharts';

const NetworkHealth = () => {
  const [summary, setSummary] = useState(null);
  const [devices, setDevices] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [summaryRes, devicesRes] = await Promise.all([
        dashboardAPI.getSummary(),
        deviceAPI.getAll(),
      ]);
      setSummary(summaryRes.data);
      setDevices(devicesRes.data);
    } catch (error) {
      console.error('Error loading network health data:', error);
    } finally {
      setLoading(false);
    }
  };

  const getHealthColor = (percentage) => {
    if (percentage >= 80) return 'text-accent-500';
    if (percentage >= 60) return 'text-warning-500';
    return 'text-danger-500';
  };

  const getHealthStatus = (percentage) => {
    if (percentage >= 80) return 'Healthy';
    if (percentage >= 60) return 'Warning';
    return 'Critical';
  };

  if (loading) {
    return <div className="text-center text-gray-400">Loading network health...</div>;
  }

  const healthData = devices.map(device => ({
    name: device.hostname,
    cpu: device.cpuUsage || 0,
    memory: device.memoryUsage || 0,
    latency: device.latency || 0,
  }));

  const networkLatencyData = devices.map(device => ({
    name: device.hostname,
    latency: device.latency || 0,
  }));

  const packetLossData = devices.map(device => ({
    name: device.hostname,
    packetLoss: device.packetLoss || 0,
  }));

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-white">Network Health</h1>
        <p className="text-gray-400 mt-1">Real-time network performance metrics</p>
      </div>

      {/* Overall Health */}
      <div className="card">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Heart className="text-primary-500" size={48} />
            <div>
              <h2 className="text-2xl font-bold text-white">Overall Network Health</h2>
              <p className="text-gray-400">Based on device status, alerts, and risk predictions</p>
            </div>
          </div>
          <div className="text-right">
            <p className={`text-5xl font-bold ${getHealthColor(summary?.networkHealthPercentage || 0)}`}>
              {summary?.networkHealthPercentage?.toFixed(1) || 0}%
            </p>
            <p className={`text-lg font-medium ${getHealthColor(summary?.networkHealthPercentage || 0)}`}>
              {getHealthStatus(summary?.networkHealthPercentage || 0)}
            </p>
          </div>
        </div>
      </div>

      {/* Health Metrics */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <div className="card">
          <div className="flex items-center gap-3 mb-2">
            <Server className="text-primary-500" size={24} />
            <span className="text-gray-400">Active Devices</span>
          </div>
          <p className="text-3xl font-bold text-white">{summary?.activeDevices || 0}</p>
          <p className="text-sm text-gray-500">of {summary?.totalDevices || 0} total</p>
        </div>

        <div className="card">
          <div className="flex items-center gap-3 mb-2">
            <Activity className="text-accent-500" size={24} />
            <span className="text-gray-400">Device Uptime</span>
          </div>
          <p className="text-3xl font-bold text-accent-500">
            {summary?.totalDevices > 0 ? ((summary?.activeDevices / summary?.totalDevices) * 100).toFixed(1) : 0}%
          </p>
          <p className="text-sm text-gray-500">average</p>
        </div>

        <div className="card">
          <div className="flex items-center gap-3 mb-2">
            <TrendingUp className="text-danger-500" size={24} />
            <span className="text-gray-400">Critical Alerts</span>
          </div>
          <p className="text-3xl font-bold text-danger-500">{summary?.criticalAlerts || 0}</p>
          <p className="text-sm text-gray-500">requiring attention</p>
        </div>

        <div className="card">
          <div className="flex items-center gap-3 mb-2">
            <TrendingDown className="text-warning-500" size={24} />
            <span className="text-gray-400">High Risk Devices</span>
          </div>
          <p className="text-3xl font-bold text-warning-500">{summary?.highRiskDevices || 0}</p>
          <p className="text-sm text-gray-500">predicted failure</p>
        </div>
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="card">
          <h2 className="text-xl font-bold text-white mb-4">CPU & Memory Usage</h2>
          <ResponsiveContainer width="100%" height={300}>
            <AreaChart data={healthData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
              <XAxis dataKey="name" stroke="#94a3b8" />
              <YAxis stroke="#94a3b8" />
              <Tooltip />
              <Area type="monotone" dataKey="cpu" stackId="1" stroke="#0ea5e9" fill="#0ea5e9" name="CPU %" />
              <Area type="monotone" dataKey="memory" stackId="2" stroke="#22c55e" fill="#22c55e" name="Memory %" />
            </AreaChart>
          </ResponsiveContainer>
        </div>

        <div className="card">
          <h2 className="text-xl font-bold text-white mb-4">Network Latency</h2>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={networkLatencyData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
              <XAxis dataKey="name" stroke="#94a3b8" />
              <YAxis stroke="#94a3b8" />
              <Tooltip />
              <Line type="monotone" dataKey="latency" stroke="#f59e0b" strokeWidth={2} name="Latency (ms)" />
            </LineChart>
          </ResponsiveContainer>
        </div>

        <div className="card lg:col-span-2">
          <h2 className="text-xl font-bold text-white mb-4">Packet Loss per Device</h2>
          <ResponsiveContainer width="100%" height={300}>
            <AreaChart data={packetLossData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
              <XAxis dataKey="name" stroke="#94a3b8" />
              <YAxis stroke="#94a3b8" />
              <Tooltip />
              <Area type="monotone" dataKey="packetLoss" stroke="#ef4444" fill="#ef4444" name="Packet Loss %" />
            </AreaChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Device Health Details */}
      <div className="card">
        <h2 className="text-xl font-bold text-white mb-4">Device Health Details</h2>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-primary-300">
                <th className="text-left py-3 px-4 text-gray-400 font-medium">Device</th>
                <th className="text-left py-3 px-4 text-gray-400 font-medium">CPU</th>
                <th className="text-left py-3 px-4 text-gray-400 font-medium">Memory</th>
                <th className="text-left py-3 px-4 text-gray-400 font-medium">Latency</th>
                <th className="text-left py-3 px-4 text-gray-400 font-medium">Packet Loss</th>
                <th className="text-left py-3 px-4 text-gray-400 font-medium">Status</th>
              </tr>
            </thead>
            <tbody>
              {devices.map((device) => (
                <tr key={device.id} className="border-b border-primary-300 hover:bg-primary-200">
                  <td className="py-3 px-4 text-white font-medium">{device.hostname}</td>
                  <td className="py-3 px-4">
                    <div className="flex items-center gap-2">
                      <div className="w-24 bg-primary-300 rounded-full h-2">
                        <div
                          className={`h-2 rounded-full ${device.cpuUsage > 80 ? 'bg-danger-500' : device.cpuUsage > 60 ? 'bg-warning-500' : 'bg-accent-500'}`}
                          style={{ width: `${device.cpuUsage || 0}%` }}
                        ></div>
                      </div>
                      <span className="text-white text-sm">{device.cpuUsage?.toFixed(1)}%</span>
                    </div>
                  </td>
                  <td className="py-3 px-4">
                    <div className="flex items-center gap-2">
                      <div className="w-24 bg-primary-300 rounded-full h-2">
                        <div
                          className={`h-2 rounded-full ${device.memoryUsage > 80 ? 'bg-danger-500' : device.memoryUsage > 60 ? 'bg-warning-500' : 'bg-accent-500'}`}
                          style={{ width: `${device.memoryUsage || 0}%` }}
                        ></div>
                      </div>
                      <span className="text-white text-sm">{device.memoryUsage?.toFixed(1)}%</span>
                    </div>
                  </td>
                  <td className="py-3 px-4 text-white">{device.latency?.toFixed(1)} ms</td>
                  <td className="py-3 px-4 text-white">{device.packetLoss?.toFixed(2)}%</td>
                  <td className="py-3 px-4">
                    <span className={`font-bold ${device.status === 'ONLINE' ? 'text-accent-500' : device.status === 'WARNING' ? 'text-warning-500' : 'text-danger-500'}`}>
                      {device.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default NetworkHealth;
