import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useTicket, useAddMessage, useUpdateTicket } from '../../hooks/useTickets';
import { useStaff } from '../../hooks/useStaff';
import { Card, CardHeader, CardContent } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Select } from '../../components/ui/Select';
import { StatusBadge } from '../../components/badges/StatusBadge';
import { PriorityBadge } from '../../components/badges/PriorityBadge';
import { StatusStepper } from '../../components/tickets/StatusStepper';
import { Thread } from '../../components/tickets/Thread';
import { ReplyBox } from '../../components/tickets/ReplyBox';
import { ArrowLeft, User, Calendar, Tag, UserCheck } from 'lucide-react';
import { formatDate } from '../../lib/utils';
import { Status } from '../../types';
import toast from 'react-hot-toast';

const statusOrder: Status[] = ['open', 'in_progress', 'resolved', 'closed'];

export function StaffTicketDetailPage() {
  const { id } = useParams<{ id: string }>();
  const { user } = useAuth();
  const navigate = useNavigate();
  
  const { data: ticket, isLoading, error } = useTicket(id!);
  const { data: staff = [] } = useStaff();
  const addMessage = useAddMessage();
  const updateTicket = useUpdateTicket();

  const handleReply = async (data: { message: string; isInternal?: boolean }) => {
    if (!user || !ticket) return;
    
    try {
      await addMessage.mutateAsync({
        ticketId: ticket.id,
        message: data.message,
        senderId: user.id,
        isInternal: data.isInternal || false,
      });
      toast.success('Message sent successfully!');
    } catch (error) {
      toast.error('Failed to send message. Please try again.');
    }
  };

  const handleStatusChange = async (newStatus: Status) => {
    if (!ticket) return;
    
    try {
      await updateTicket.mutateAsync({
        id: ticket.id,
        updates: { status: newStatus },
      });
      toast.success('Status updated successfully!');
    } catch (error) {
      toast.error('Failed to update status. Please try again.');
    }
  };

  const handleAssigneeChange = async (assignedStaffId: string) => {
    if (!ticket) return;
    
    try {
      await updateTicket.mutateAsync({
        id: ticket.id,
        updates: { assignedStaffId: assignedStaffId || undefined },
      });
      toast.success('Assignee updated successfully!');
    } catch (error) {
      toast.error('Failed to update assignee. Please try again.');
    }
  };

  if (isLoading) {
    return (
      <div className="max-w-4xl mx-auto space-y-6">
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-12 text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-4 text-muted">Loading ticket details...</p>
        </div>
      </div>
    );
  }

  if (error || !ticket) {
    return (
      <div className="max-w-4xl mx-auto space-y-6">
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-12 text-center">
          <p className="text-red-600 text-lg">Ticket not found</p>
          <Button onClick={() => navigate('/staff/tickets')} className="mt-4">
            Back to Tickets
          </Button>
        </div>
      </div>
    );
  }

  const allUsers = [ticket.customer, ticket.assignedStaff, ...staff].filter(Boolean) as any[];
  const currentStatusIndex = statusOrder.indexOf(ticket.status);
  const availableStatuses = statusOrder.slice(currentStatusIndex);

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      <div className="flex items-center space-x-4">
        <Button
          variant="ghost"
          onClick={() => navigate('/staff/tickets')}
          className="flex items-center space-x-2"
        >
          <ArrowLeft className="h-4 w-4" />
          <span>Back to Tickets</span>
        </Button>
      </div>

      {/* Ticket Header */}
      <Card>
        <CardHeader>
          <div className="flex items-start justify-between">
            <div>
              <div className="flex items-center space-x-3 mb-2">
                <h1 className="text-2xl font-bold text-ink">{ticket.subject}</h1>
                <span className="text-sm font-mono text-muted">#{ticket.id}</span>
              </div>
              <p className="text-muted">{ticket.description}</p>
            </div>
            <div className="flex items-center space-x-2">
              <PriorityBadge priority={ticket.priority} />
              <StatusBadge status={ticket.status} />
            </div>
          </div>
        </CardHeader>
        
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-6">
            <div className="flex items-center space-x-2">
              <User className="h-4 w-4 text-muted" />
              <div>
                <p className="text-sm text-muted">Customer</p>
                <p className="font-medium text-ink">{ticket.customer.name}</p>
              </div>
            </div>
            
            <div className="flex items-center space-x-2">
              <Calendar className="h-4 w-4 text-muted" />
              <div>
                <p className="text-sm text-muted">Created</p>
                <p className="font-medium text-ink">{formatDate(ticket.createdAt)}</p>
              </div>
            </div>
            
            <div className="flex items-center space-x-2">
              <Tag className="h-4 w-4 text-muted" />
              <div>
                <p className="text-sm text-muted">Category</p>
                <p className="font-medium text-ink">{ticket.category.name}</p>
              </div>
            </div>

            <div className="flex items-center space-x-2">
              <UserCheck className="h-4 w-4 text-muted" />
              <div>
                <p className="text-sm text-muted">Assignee</p>
                <p className="font-medium text-ink">
                  {ticket.assignedStaff?.name || 'Unassigned'}
                </p>
              </div>
            </div>
          </div>
          
          <StatusStepper currentStatus={ticket.status} />
        </CardContent>
      </Card>

      {/* Actions */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <Card>
          <CardHeader>
            <h3 className="text-lg font-semibold text-ink">Update Status</h3>
          </CardHeader>
          <CardContent>
            <Select
              value={ticket.status}
              onChange={(e) => handleStatusChange(e.target.value as Status)}
            >
              {availableStatuses.map((status) => (
                <option key={status} value={status}>
                  {status.replace('_', ' ').replace(/\b\w/g, l => l.toUpperCase())}
                </option>
              ))}
            </Select>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <h3 className="text-lg font-semibold text-ink">Assign to Staff</h3>
          </CardHeader>
          <CardContent>
            <Select
              value={ticket.assignedStaffId || ''}
              onChange={(e) => handleAssigneeChange(e.target.value)}
            >
              <option value="">Unassigned</option>
              {staff.map((member) => (
                <option key={member.id} value={member.id}>
                  {member.name}
                </option>
              ))}
            </Select>
          </CardContent>
        </Card>
      </div>

      {/* Messages Thread */}
      <Card>
        <CardHeader>
          <h2 className="text-lg font-semibold text-ink">Conversation</h2>
        </CardHeader>
        <CardContent>
          <Thread
            messages={ticket.messages}
            users={allUsers}
            currentUserId={user?.id}
          />
        </CardContent>
      </Card>

      {/* Reply Box */}
      <ReplyBox
        onSubmit={handleReply}
        isLoading={addMessage.isPending}
        disabled={ticket.status === 'closed'}
        allowInternal={true}
      />
    </div>
  );
}