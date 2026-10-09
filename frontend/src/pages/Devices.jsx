import { useState, useEffect } from 'react';
import { deviceAPI, predictionAPI } from '../services/api';
import { Server, Cpu, HardDrive, Activity, AlertTriangle } from 'lucide-react';

const Devices = () => {
  const [devices, setDevices] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDevices();
  }, []);

  const loadDevices = async () => {
    try {
      const response = await deviceAPI.getAll();
      setDevices(response.data);
    } catch (error) {
      console.error('Error loading devices:', error);
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'ONLINE': return 'text-accent-500';
      case 'WARNING': return 'text-warning-500';
      case 'OFFLINE': return 'text-danger-500';
      case 'CRITICAL': return 'text-danger-600';
      default: return 'text-gray-400';
    }
  };

  const getRiskColor = (riskLevel) => {
    switch (riskLevel) {
      case 'CRITICAL': return 'bg-danger-600';
      case 'HIGH': return 'bg-danger-500';
      case 'MEDIUM': return 'bg-warning-500';
      case 'LOW': return 'bg-accent-500';
      default: return 'bg-primary-500';
    }
  };

  if (loading) {
    return <div className="text-center text-gray-400">Loading devices...</div>;
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-white">Devices</h1>
        <p className="text-gray-400 mt-1">Network device inventory and status</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {devices.map((device) => (
          <div key={device.id} className="card">
            <div className="flex items-start justify-between mb-4">
              <div className="flex items-center gap-3">
                <Server className="text-primary-500" size={32} />
                <div>
                  <h3 className="text-xl font-bold text-white">{device.hostname}</h3>
                  <p className="text-gray-400 text-sm">{device.ipAddress}</p>
                </div>
              </div>
              <span className={`font-bold ${getStatusColor(device.status)}`}>
                {device.status}
              </span>
            </div>

            <div className="space-y-3">
              <div className="flex items-center justify-between">
                <span className="text-gray-400 text-sm">Type</span>
                <span className="text-white">{device.deviceType}</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-gray-400 text-sm">Location</span>
                <span className="text-white">{device.location}</span>
              </div>

              <div className="border-t border-primary-300 pt-3 mt-3">
                <div className="flex items-center gap-2 mb-2">
                  <Cpu size={16} className="text-primary-500" />
                  <span className="text-gray-400 text-sm">CPU</span>
                  <span className="text-white ml-auto">{device.cpuUsage?.toFixed(1)}%</span>
                </div>
                <div className="flex items-center gap-2 mb-2">
                  <HardDrive size={16} className="text-primary-500" />
                  <span className="text-gray-400 text-sm">Memory</span>
                  <span className="text-white ml-auto">{device.memoryUsage?.toFixed(1)}%</span>
                </div>
                <div className="flex items-center gap-2 mb-2">
                  <Activity size={16} className="text-primary-500" />
                  <span className="text-gray-400 text-sm">Latency</span>
                  <span className="text-white ml-auto">{device.latency?.toFixed(1)} ms</span>
                </div>
                <div className="flex items-center gap-2">
                  <AlertTriangle size={16} className="text-primary-500" />
                  <span className="text-gray-400 text-sm">Packet Loss</span>
                  <span className="text-white ml-auto">{device.packetLoss?.toFixed(2)}%</span>
                </div>
              </div>

              {device.riskLevel && (
                <div className="mt-3 pt-3 border-t border-primary-300">
                  <div className="flex items-center justify-between">
                    <span className="text-gray-400 text-sm">AI Risk Level</span>
                    <span className={`px-2 py-1 rounded text-xs font-bold text-white ${getRiskColor(device.riskLevel)}`}>
                      {device.riskLevel}
                    </span>
                  </div>
                  {device.failureProbability && (
                    <div className="flex items-center justify-between mt-2">
                      <span className="text-gray-400 text-sm">Failure Probability</span>
                      <span className="text-white">{(device.failureProbability * 100).toFixed(1)}%</span>
                    </div>
                  )}
                </div>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Devices;
