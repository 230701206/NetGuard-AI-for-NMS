import axios from 'axios';

const API_BASE_URL = '/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const dashboardAPI = {
  getSummary: () => api.get('/dashboard/summary'),
  getRecentAlerts: (limit = 10) => api.get(`/dashboard/alerts?limit=${limit}`),
  getRecentIncidents: (limit = 5) => api.get(`/dashboard/incidents?limit=${limit}`),
  getMetrics: () => api.get('/dashboard/metrics'),
};

export const deviceAPI = {
  getAll: () => api.get('/devices'),
  getById: (id) => api.get(`/devices/${id}`),
  create: (data) => api.post('/devices', data),
  update: (id, data) => api.put(`/devices/${id}`, data),
  delete: (id) => api.delete(`/devices/${id}`),
};

export const alertAPI = {
  getAll: () => api.get('/alerts'),
  getById: (id) => api.get(`/alerts/${id}`),
  create: (data) => api.post('/alerts', data),
  update: (id, data) => api.put(`/alerts/${id}`, data),
  delete: (id) => api.delete(`/alerts/${id}`),
  getByDevice: (deviceId) => api.get(`/alerts/device/${deviceId}`),
  getRecent: (hours = 24) => api.get(`/alerts/recent?hours=${hours}`),
};

export const incidentAPI = {
  getAll: () => api.get('/incidents'),
  getById: (id) => api.get(`/incidents/${id}`),
  create: (data) => api.post('/incidents', data),
  resolve: (id) => api.post(`/incidents/${id}/resolve`),
  getOpen: () => api.get('/incidents/open'),
};

export const correlationAPI = {
  run: () => api.post('/correlation/run'),
  getResults: () => api.get('/correlation/results'),
};

export const rootCauseAPI = {
  analyze: (incidentId) => api.get(`/root-cause/incident/${incidentId}`),
};

export const predictionAPI = {
  getAll: () => api.get('/predictions'),
  getById: (id) => api.get(`/predictions/${id}`),
  getByDevice: (deviceId) => api.get(`/predictions/device/${deviceId}`),
  runAll: () => api.post('/predictions/run'),
  runForDevice: (deviceId) => api.post(`/predictions/device/${deviceId}/run`),
};

export const simulationAPI = {
  routerFailure: () => api.post('/simulation/router-failure'),
  packetLoss: () => api.post('/simulation/packet-loss'),
  highCPU: () => api.post('/simulation/high-cpu'),
  randomAlerts: (count = 5) => api.post(`/simulation/random-alerts?count=${count}`),
};

export default api;
