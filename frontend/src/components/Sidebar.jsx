import { Link, useLocation } from 'react-router-dom';
import { Activity, Server, AlertTriangle, FileText, Search, TrendingUp, Heart, Settings } from 'lucide-react';

const Sidebar = () => {
  const location = useLocation();

  const menuItems = [
    { path: '/', label: 'Dashboard', icon: Activity },
    { path: '/devices', label: 'Devices', icon: Server },
    { path: '/alerts', label: 'Alerts', icon: AlertTriangle },
    { path: '/incidents', label: 'Incidents', icon: FileText },
    { path: '/root-cause', label: 'Root Cause', icon: Search },
    { path: '/predictions', label: 'Predictions', icon: TrendingUp },
    { path: '/network-health', label: 'Network Health', icon: Heart },
    { path: '/settings', label: 'Settings', icon: Settings },
  ];

  return (
    <aside className="fixed left-0 top-0 h-full w-64 bg-primary-100 border-r border-primary-300 shadow-xl">
      <div className="p-6 border-b border-primary-300">
        <h1 className="text-2xl font-bold text-primary-500">NETGUARD AI</h1>
        <p className="text-sm text-gray-400 mt-1">Network Management System</p>
      </div>

      <nav className="p-4">
        <ul className="space-y-2">
          {menuItems.map((item) => {
            const Icon = item.icon;
            const isActive = location.pathname === item.path;

            return (
              <li key={item.path}>
                <Link
                  to={item.path}
                  className={`flex items-center gap-3 px-4 py-3 rounded-lg transition-colors ${
                    isActive
                      ? 'bg-primary-500 text-white'
                      : 'text-gray-300 hover:bg-primary-200 hover:text-white'
                  }`}
                >
                  <Icon size={20} />
                  <span className="font-medium">{item.label}</span>
                </Link>
              </li>
            );
          })}
        </ul>
      </nav>

      <div className="absolute bottom-0 left-0 right-0 p-4 border-t border-primary-300">
        <div className="text-xs text-gray-500 text-center">
          NetGuard AI v1.0.0
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;
