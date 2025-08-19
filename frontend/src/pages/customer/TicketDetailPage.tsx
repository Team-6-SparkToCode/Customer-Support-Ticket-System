import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useTicket, useAddMessage } from '../../hooks/useTickets';
import { Card, CardHeader, CardContent } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { StatusBadge } from '../../components/badges/StatusBadge';
import { PriorityBadge } from '../../components/badges/PriorityBadge';
import { StatusStepper } from '../../components/tickets/StatusStepper';
import { Thread } from '../../components/tickets/Thread';
import { ReplyBox } from '../../components/tickets/ReplyBox';
import { ArrowLeft, User, Calendar, Tag } from 'lucide-react';
import { formatDate } from '../../lib/utils';
import toast from 'react-hot-toast';

export function TicketDetailPage() {
  const { id } = useParams<{ id: string }>();
  const { user } = useAuth();
  const navigate = useNavigate();
  
  const { data: ticket, isLoading, error } = useTicket(id!);
  const addMessage = useAddMessage();

  const handleReply = async (data: { message: string }) => {
    if (!user || !ticket) return;
    
    try {
      await addMessage.mutateAsync({
        ticketId: ticket.id,
        message: data.message,
        senderId: user.id,
      });
      toast.success('Message sent successfully!');
    } catch (error) {
      toast.error('Failed to send message. Please try again.');
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
          <Button onClick={() => navigate('/my/tickets')} className="mt-4">
            Back to Tickets
          </Button>
        </div>
      </div>
    );
  }

  const allUsers = [ticket.customer, ticket.assignedStaff].filter(Boolean) as any[];

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      <div className="flex items-center space-x-4">
        <Button
          variant="ghost"
          onClick={() => navigate('/my/tickets')}
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
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
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
          </div>
          
          <StatusStepper currentStatus={ticket.status} />
        </CardContent>
      </Card>

      {/* Messages Thread */}
      <Card>
        <CardHeader>
          <h2 className="text-lg font-semibold text-ink">Conversation</h2>
        </CardHeader>
        <CardContent>
          <Thread
            messages={ticket.messages.filter(m => !m.isInternal)}
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
      />
    </div>
  );
}