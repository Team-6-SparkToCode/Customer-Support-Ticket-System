import React from 'react';
import { Outlet, Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { RoleBadge } from '../badges/RoleBadge';
import { Button } from '../ui/Button';
import { 
  Home, 
  Ticket, 
  Plus, 
  Users, 
  Settings, 
  LayoutDashboard,
  LogOut,
  MessageCircle
} from 'lucide-react';

export function AppShell() {
  const { user, logout } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();

  if (!user) return null;

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const getNavItems = () => {
    switch (user.role) {
      case 'customer':
        return [
          { to: '/my/tickets', label: 'My Tickets', icon: Ticket },
          { to: '/my/tickets/new', label: 'New Ticket', icon: Plus },
        ];
      case 'staff':
        return [
          { to: '/staff/tickets', label: 'All Tickets', icon: Ticket },
          { to: '/staff/dashboard', label: 'Dashboard', icon: LayoutDashboard },
        ];
      case 'admin':
        return [
          { to: '/staff/tickets', label: 'All Tickets', icon: Ticket },
          { to: '/staff/dashboard', label: 'Dashboard', icon: LayoutDashboard },
          { to: '/admin/categories', label: 'Categories', icon: Settings },
          { to: '/admin/priorities', label: 'Priorities', icon: Settings },
          { to: '/admin/staff', label: 'Staff', icon: Users },
        ];
      default:
        return [];
    }
  };

  return (
    <div className="min-h-screen bg-bg flex">
      {/* Sidebar */}
      <div className="w-64 bg-white shadow-sm border-r border-gray-200">
        <div className="p-6 border-b border-gray-200">
          <div className="flex items-center space-x-3">
            <MessageCircle className="h-8 w-8 text-blue-600" />
            <div>
              <h1 className="text-xl font-bold text-ink">Support</h1>
              <p className="text-sm text-muted">Ticket System</p>
            </div>
          </div>
        </div>

        <nav className="p-4 space-y-2">
          {getNavItems().map((item) => (
            <Link
              key={item.to}
              to={item.to}
              className={`flex items-center space-x-3 px-3 py-2 rounded-2xl transition-colors ${
                location.pathname === item.to
                  ? 'bg-blue-100 text-blue-700'
                  : 'text-muted hover:bg-gray-100 hover:text-ink'
              }`}
            >
              <item.icon className="h-5 w-5" />
              <span className="font-medium">{item.label}</span>
            </Link>
          ))}
        </nav>

        <div className="absolute bottom-0 w-64 p-4 border-t border-gray-200 bg-white">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-ink">{user.name}</p>
              <div className="flex items-center space-x-2 mt-1">
                <RoleBadge role={user.role} />
              </div>
            </div>
            <Button
              variant="ghost"
              size="sm"
              onClick={handleLogout}
            >
              <LogOut className="h-4 w-4" />
            </Button>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1 overflow-auto">
        <div className="p-6">
          <Outlet />
        </div>
      </div>
    </div>
  );
}