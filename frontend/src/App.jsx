import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import Devices from './pages/Devices';
import Alerts from './pages/Alerts';
import Incidents from './pages/Incidents';
import RootCause from './pages/RootCause';
import Predictions from './pages/Predictions';
import NetworkHealth from './pages/NetworkHealth';
import Settings from './pages/Settings';

function App() {
  return (
    <Router>
      <div className="flex min-h-screen">
        <Sidebar />
        <main className="flex-1 ml-64 p-8">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/devices" element={<Devices />} />
            <Route path="/alerts" element={<Alerts />} />
            <Route path="/incidents" element={<Incidents />} />
            <Route path="/root-cause" element={<RootCause />} />
            <Route path="/predictions" element={<Predictions />} />
            <Route path="/network-health" element={<NetworkHealth />} />
            <Route path="/settings" element={<Settings />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
