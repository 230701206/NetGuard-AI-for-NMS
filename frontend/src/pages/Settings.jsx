import { Info, Database, Brain, Server as ServerIcon, Code, Book } from 'lucide-react';

const Settings = () => {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-white">Settings & About</h1>
        <p className="text-gray-400 mt-1">System information and configuration</p>
      </div>

      {/* About Section */}
      <div className="card">
        <div className="flex items-center gap-3 mb-4">
          <Info className="text-primary-500" size={28} />
          <h2 className="text-xl font-bold text-white">About NetGuard AI</h2>
        </div>
        <div className="space-y-4 text-gray-300">
          <p>
            <strong className="text-white">NetGuard AI</strong> is a Predictive Alert-Correlation and Root-Cause Assistant
            for Network Management Systems. It uses artificial intelligence and machine learning to analyze network alerts,
            correlate related events, identify root causes, and predict potential failures before they occur.
          </p>
          <div className="grid grid-cols-2 gap-4 mt-4">
            <div className="bg-primary-200 p-4 rounded">
              <p className="text-gray-400 text-sm">Version</p>
              <p className="text-white font-bold">1.0.0</p>
            </div>
            <div className="bg-primary-200 p-4 rounded">
              <p className="text-gray-400 text-sm">Build</p>
              <p className="text-white font-bold">Final Year Project</p>
            </div>
          </div>
        </div>
      </div>

      {/* Architecture */}
      <div className="card">
        <div className="flex items-center gap-3 mb-4">
          <ServerIcon className="text-primary-500" size={28} />
          <h2 className="text-xl font-bold text-white">System Architecture</h2>
        </div>
        <div className="space-y-3 text-gray-300">
          <div className="flex items-start gap-3">
            <div className="bg-primary-500 p-2 rounded">
              <ServerIcon size={20} className="text-white" />
            </div>
            <div>
              <p className="text-white font-medium">Backend</p>
              <p className="text-sm">Spring Boot 3.x with Java 17, REST APIs, JPA/Hibernate</p>
            </div>
          </div>
          <div className="flex items-start gap-3">
            <div className="bg-primary-500 p-2 rounded">
              <Database size={20} className="text-white" />
            </div>
            <div>
              <p className="text-white font-medium">Database</p>
              <p className="text-sm">MySQL 8 for data persistence</p>
            </div>
          </div>
          <div className="flex items-start gap-3">
            <div className="bg-primary-500 p-2 rounded">
              <Brain size={20} className="text-white" />
            </div>
            <div>
              <p className="text-white font-medium">AI/ML Engine</p>
              <p className="text-sm">Python FastAPI with scikit-learn for predictions (optional)</p>
            </div>
          </div>
          <div className="flex items-start gap-3">
            <div className="bg-primary-500 p-2 rounded">
              <Code size={20} className="text-white" />
            </div>
            <div>
              <p className="text-white font-medium">Frontend</p>
              <p className="text-sm">React with Vite, Tailwind CSS, Recharts for visualization</p>
            </div>
          </div>
        </div>
      </div>

      {/* Key Features */}
      <div className="card">
        <div className="flex items-center gap-3 mb-4">
          <Book className="text-primary-500" size={28} />
          <h2 className="text-xl font-bold text-white">Key Features</h2>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="bg-primary-200 p-4 rounded">
            <h3 className="text-white font-bold mb-2">Alert Correlation</h3>
            <p className="text-gray-400 text-sm">
              Groups related alerts based on time proximity, device topology, and alert patterns
            </p>
          </div>
          <div className="bg-primary-200 p-4 rounded">
            <h3 className="text-white font-bold mb-2">Root Cause Analysis</h3>
            <p className="text-gray-400 text-sm">
              AI-powered identification of probable root causes with confidence scores
            </p>
          </div>
          <div className="bg-primary-200 p-4 rounded">
            <h3 className="text-white font-bold mb-2">Predictive Analytics</h3>
            <p className="text-gray-400 text-sm">
              Machine learning models predict device failures before they occur
            </p>
          </div>
          <div className="bg-primary-200 p-4 rounded">
            <h3 className="text-white font-bold mb-2">Real-time Dashboard</h3>
            <p className="text-gray-400 text-sm">
              Live monitoring with charts, metrics, and alert feeds
            </p>
          </div>
          <div className="bg-primary-200 p-4 rounded">
            <h3 className="text-white font-bold mb-2">Incident Management</h3>
            <p className="text-gray-400 text-sm">
              Track and resolve network incidents with AI recommendations
            </p>
          </div>
          <div className="bg-primary-200 p-4 rounded">
            <h3 className="text-white font-bold mb-2">Simulation Mode</h3>
            <p className="text-gray-400 text-sm">
              Simulate network failures for testing and demonstration
            </p>
          </div>
        </div>
      </div>

      {/* Technologies */}
      <div className="card">
        <div className="flex items-center gap-3 mb-4">
          <Code className="text-primary-500" size={28} />
          <h2 className="text-xl font-bold text-white">Technologies Used</h2>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <h3 className="text-white font-bold mb-2">Backend</h3>
            <ul className="text-gray-400 text-sm space-y-1">
              <li>• Java 17</li>
              <li>• Spring Boot 3.2.0</li>
              <li>• Spring Data JPA</li>
              <li>• Spring Validation</li>
              <li>• Lombok</li>
              <li>• Maven</li>
            </ul>
          </div>
          <div>
            <h3 className="text-white font-bold mb-2">Frontend</h3>
            <ul className="text-gray-400 text-sm space-y-1">
              <li>• React 18</li>
              <li>• Vite</li>
              <li>• Tailwind CSS</li>
              <li>• Recharts</li>
              <li>• Axios</li>
              <li>• Lucide React</li>
            </ul>
          </div>
          <div>
            <h3 className="text-white font-bold mb-2">AI/ML & Database</h3>
            <ul className="text-gray-400 text-sm space-y-1">
              <li>• Python 3.x</li>
              <li>• FastAPI</li>
              <li>• scikit-learn</li>
              <li>• MySQL 8</li>
              <li>• REST APIs</li>
              <li>• CORS Configuration</li>
            </ul>
          </div>
        </div>
      </div>

      {/* Demo Information */}
      <div className="card border-accent-500">
        <div className="flex items-center gap-3 mb-4">
          <Info className="text-accent-500" size={28} />
          <h2 className="text-xl font-bold text-white">Demo Instructions</h2>
        </div>
        <div className="space-y-3 text-gray-300">
          <p><strong className="text-white">1. Dashboard:</strong> View overall network health, recent alerts, and incidents</p>
          <p><strong className="text-white">2. Simulate Router Failure:</strong> Click the button on Dashboard to generate realistic alert chains</p>
          <p><strong className="text-white">3. Devices:</strong> View all network devices with health metrics and risk predictions</p>
          <p><strong className="text-white">4. Alerts:</strong> View all alerts and run correlation analysis</p>
          <p><strong className="text-white">5. Incidents:</strong> View correlated incidents and analyze root causes</p>
          <p><strong className="text-white">6. Predictions:</strong> View AI-powered failure risk predictions</p>
          <p><strong className="text-white">7. Network Health:</strong> Monitor real-time network performance metrics</p>
        </div>
      </div>
    </div>
  );
};

export default Settings;
