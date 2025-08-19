import React from 'react';
import { useTickets } from '../../hooks/useTickets';
import { Card, CardHeader, CardContent } from '../../components/ui/Card';
import { StatusBadge } from '../../components/badges/StatusBadge';
import { PriorityBadge } from '../../components/badges/PriorityBadge';
import { 
  BarChart3, 
  Clock, 
  CheckCircle, 
  AlertCircle, 
  Users,
  Ticket
} from 'lucide-react';

export function StaffDashboardPage() {
  const { data: allTickets = [] } = useTickets();

  const stats = React.useMemo(() => {
    const total = allTickets.length;
    const open = allTickets.filter(t => t.status === 'open').length;
    const inProgress = allTickets.filter(t => t.status === 'in_progress').length;
    const resolved = allTickets.filter(t => t.status === 'resolved').length;
    const closed = allTickets.filter(t => t.status === 'closed').length;
    
    const urgent = allTickets.filter(t => t.priority === 'urgent').length;
    const high = allTickets.filter(t => t.priority === 'high').length;
    
    const unassigned = allTickets.filter(t => !t.assignedStaffId).length;
    
    return {
      total,
      open,
      inProgress,
      resolved,
      closed,
      urgent,
      high,
      unassigned,
    };
  }, [allTickets]);

  const recentTickets = React.useMemo(() => {
    return allTickets
      .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
      .slice(0, 5);
  }, [allTickets]);

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-ink">Dashboard</h1>
        <p className="text-muted mt-2">Overview of support ticket system</p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card>
          <CardContent>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted">Total Tickets</p>
                <p className="text-2xl font-bold text-ink">{stats.total}</p>
              </div>
              <Ticket className="h-8 w-8 text-blue-600" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted">Open Tickets</p>
                <p className="text-2xl font-bold text-yellow-600">{stats.open}</p>
              </div>
              <Clock className="h-8 w-8 text-yellow-600" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted">In Progress</p>
                <p className="text-2xl font-bold text-blue-600">{stats.inProgress}</p>
              </div>
              <BarChart3 className="h-8 w-8 text-blue-600" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted">Resolved</p>
                <p className="text-2xl font-bold text-green-600">{stats.resolved}</p>
              </div>
              <CheckCircle className="h-8 w-8 text-green-600" />
            </div>
          </CardContent>
        </Card>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Priority Overview */}
        <Card>
          <CardHeader>
            <h2 className="text-lg font-semibold text-ink">Priority Overview</h2>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <div className="flex items-center space-x-2">
                  <AlertCircle className="h-4 w-4 text-red-600" />
                  <span className="text-sm font-medium">Urgent</span>
                </div>
                <span className="text-xl font-bold text-red-600">{stats.urgent}</span>
              </div>
              
              <div className="flex items-center justify-between">
                <div className="flex items-center space-x-2">
                  <AlertCircle className="h-4 w-4 text-orange-600" />
                  <span className="text-sm font-medium">High</span>
                </div>
                <span className="text-xl font-bold text-orange-600">{stats.high}</span>
              </div>
              
              <div className="flex items-center justify-between">
                <div className="flex items-center space-x-2">
                  <Users className="h-4 w-4 text-gray-600" />
                  <span className="text-sm font-medium">Unassigned</span>
                </div>
                <span className="text-xl font-bold text-gray-600">{stats.unassigned}</span>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Recent Tickets */}
        <Card>
          <CardHeader>
            <h2 className="text-lg font-semibold text-ink">Recent Tickets</h2>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {recentTickets.map((ticket) => (
                <div key={ticket.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-2xl">
                  <div className="flex-1">
                    <p className="text-sm font-medium text-ink truncate">
                      #{ticket.id} - {ticket.subject}
                    </p>
                    <p className="text-xs text-muted">
                      {ticket.customer?.name}
                    </p>
                  </div>
                  <div className="flex items-center space-x-2 ml-4">
                    <PriorityBadge priority={ticket.priority} />
                    <StatusBadge status={ticket.status} />
                  </div>
                </div>
              ))}
              
              {recentTickets.length === 0 && (
                <p className="text-center text-muted">No tickets found</p>
              )}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}