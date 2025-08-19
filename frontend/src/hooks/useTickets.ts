import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { TicketWithMessages, Ticket, TicketMessage } from '../types';

const API_BASE = '/api';

export function useTickets(params: Record<string, any> = {}) {
  return useQuery<TicketWithMessages[]>({
    queryKey: ['tickets', params],
    queryFn: async () => {
      const searchParams = new URLSearchParams();
      Object.entries(params).forEach(([key, value]) => {
        if (value !== undefined && value !== '' && value !== 'all') {
          searchParams.append(key, value.toString());
        }
      });
      
      const response = await fetch(`${API_BASE}/tickets?${searchParams}`);
      if (!response.ok) throw new Error('Failed to fetch tickets');
      return response.json();
    },
  });
}

export function useTicket(id: string) {
  return useQuery<TicketWithMessages>({
    queryKey: ['ticket', id],
    queryFn: async () => {
      const response = await fetch(`${API_BASE}/tickets/${id}`);
      if (!response.ok) throw new Error('Failed to fetch ticket');
      return response.json();
    },
    enabled: !!id,
  });
}

export function useCreateTicket() {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: async (ticket: Omit<Ticket, 'id' | 'status' | 'createdAt' | 'updatedAt'>) => {
      const response = await fetch(`${API_BASE}/tickets`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(ticket),
      });
      if (!response.ok) throw new Error('Failed to create ticket');
      return response.json();
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['tickets'] });
    },
  });
}

export function useUpdateTicket() {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: async ({ id, updates }: { id: string; updates: Partial<Ticket> }) => {
      const response = await fetch(`${API_BASE}/tickets/${id}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updates),
      });
      if (!response.ok) throw new Error('Failed to update ticket');
      return response.json();
    },
    onSuccess: (_, { id }) => {
      queryClient.invalidateQueries({ queryKey: ['tickets'] });
      queryClient.invalidateQueries({ queryKey: ['ticket', id] });
    },
  });
}

export function useAddMessage() {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: async ({ 
      ticketId, 
      message, 
      senderId, 
      isInternal = false 
    }: { 
      ticketId: string; 
      message: string; 
      senderId: string; 
      isInternal?: boolean;
    }) => {
      const response = await fetch(`${API_BASE}/tickets/${ticketId}/messages`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ message, senderId, isInternal }),
      });
      if (!response.ok) throw new Error('Failed to add message');
      return response.json();
    },
    onSuccess: (_, { ticketId }) => {
      queryClient.invalidateQueries({ queryKey: ['tickets'] });
      queryClient.invalidateQueries({ queryKey: ['ticket', ticketId] });
    },
  });
}