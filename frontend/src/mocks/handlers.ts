import { http, HttpResponse } from 'msw';
import { mockUsers, mockCategories, mockPriorities, mockTickets, mockTicketMessages } from './data';
import { Ticket, TicketMessage, TicketWithMessages } from '../types';

let tickets = [...mockTickets];
let messages = [...mockTicketMessages];
let categories = [...mockCategories];
let priorities = [...mockPriorities];

export const handlers = [
  // Auth endpoints
  http.get('/api/me', ({ request }) => {
    const authHeader = request.headers.get('Authorization');
    if (!authHeader) {
      return HttpResponse.json({ error: 'Unauthorized' }, { status: 401 });
    }
    
    // Mock user based on token (simplified)
    const user = mockUsers[0]; // Default to customer for demo
    return HttpResponse.json(user);
  }),

  http.post('/api/login', async ({ request }) => {
    const body = await request.json() as { email: string; password: string; role: string };
    
    const user = mockUsers.find(u => u.email === body.email && u.role === body.role);
    if (!user) {
      return HttpResponse.json({ error: 'Invalid credentials' }, { status: 401 });
    }

    return HttpResponse.json({
      token: `mock-token-${user.id}`,
      user,
    });
  }),

  // Categories
  http.get('/api/categories', () => {
    return HttpResponse.json(categories);
  }),

  http.post('/api/categories', async ({ request }) => {
    const body = await request.json() as { name: string; description?: string };
    const newCategory = {
      id: Date.now().toString(),
      name: body.name,
      description: body.description,
    };
    categories.push(newCategory);
    return HttpResponse.json(newCategory);
  }),

  http.put('/api/categories/:id', async ({ params, request }) => {
    const body = await request.json() as { name: string; description?: string };
    const index = categories.findIndex(c => c.id === params.id);
    if (index !== -1) {
      categories[index] = { ...categories[index], ...body };
      return HttpResponse.json(categories[index]);
    }
    return HttpResponse.json({ error: 'Not found' }, { status: 404 });
  }),

  http.delete('/api/categories/:id', ({ params }) => {
    const index = categories.findIndex(c => c.id === params.id);
    if (index !== -1) {
      categories.splice(index, 1);
      return HttpResponse.json({ success: true });
    }
    return HttpResponse.json({ error: 'Not found' }, { status: 404 });
  }),

  // Priorities
  http.get('/api/priorities', () => {
    return HttpResponse.json(priorities);
  }),

  // Tickets
  http.get('/api/tickets', ({ request }) => {
    const url = new URL(request.url);
    const mine = url.searchParams.get('mine');
    const status = url.searchParams.get('status');
    const category = url.searchParams.get('category');
    const assignee = url.searchParams.get('assignee');
    const priority = url.searchParams.get('priority');
    const q = url.searchParams.get('q');

    let filteredTickets = [...tickets];

    if (mine === 'true') {
      filteredTickets = filteredTickets.filter(t => t.customerId === '1'); // Mock current user
    }

    if (status && status !== 'all') {
      filteredTickets = filteredTickets.filter(t => t.status === status);
    }

    if (category && category !== 'all') {
      filteredTickets = filteredTickets.filter(t => t.categoryId === category);
    }

    if (assignee && assignee !== 'all') {
      filteredTickets = filteredTickets.filter(t => t.assignedStaffId === assignee);
    }

    if (priority && priority !== 'all') {
      filteredTickets = filteredTickets.filter(t => t.priority === priority);
    }

    if (q) {
      const query = q.toLowerCase();
      filteredTickets = filteredTickets.filter(t => 
        t.subject.toLowerCase().includes(query) ||
        t.description.toLowerCase().includes(query) ||
        t.id.toLowerCase().includes(query)
      );
    }

    // Add related data
    const ticketsWithData = filteredTickets.map(ticket => {
      const customer = mockUsers.find(u => u.id === ticket.customerId);
      const assignedStaff = ticket.assignedStaffId ? mockUsers.find(u => u.id === ticket.assignedStaffId) : undefined;
      const category = categories.find(c => c.id === ticket.categoryId);
      
      return {
        ...ticket,
        customer,
        assignedStaff,
        category,
      };
    });

    return HttpResponse.json(ticketsWithData);
  }),

  http.post('/api/tickets', async ({ request }) => {
    const body = await request.json() as Omit<Ticket, 'id' | 'status' | 'createdAt' | 'updatedAt'>;
    const newTicket: Ticket = {
      ...body,
      id: Date.now().toString(),
      status: 'open',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    };
    
    tickets.push(newTicket);
    
    // Add initial message
    const initialMessage: TicketMessage = {
      id: Date.now().toString() + '-msg',
      ticketId: newTicket.id,
      senderId: body.customerId,
      message: body.description,
      createdAt: new Date().toISOString(),
    };
    messages.push(initialMessage);
    
    return HttpResponse.json(newTicket);
  }),

  http.get('/api/tickets/:id', ({ params }) => {
    const ticket = tickets.find(t => t.id === params.id);
    if (!ticket) {
      return HttpResponse.json({ error: 'Ticket not found' }, { status: 404 });
    }

    const ticketMessages = messages.filter(m => m.ticketId === ticket.id);
    const customer = mockUsers.find(u => u.id === ticket.customerId);
    const assignedStaff = ticket.assignedStaffId ? mockUsers.find(u => u.id === ticket.assignedStaffId) : undefined;
    const category = categories.find(c => c.id === ticket.categoryId);

    const result: TicketWithMessages = {
      ...ticket,
      messages: ticketMessages,
      customer: customer!,
      assignedStaff,
      category: category!,
    };

    return HttpResponse.json(result);
  }),

  http.post('/api/tickets/:id/messages', async ({ params, request }) => {
    const body = await request.json() as { message: string; senderId: string; isInternal?: boolean };
    const newMessage: TicketMessage = {
      id: Date.now().toString(),
      ticketId: params.id as string,
      senderId: body.senderId,
      message: body.message,
      isInternal: body.isInternal,
      createdAt: new Date().toISOString(),
    };
    
    messages.push(newMessage);
    
    // Update ticket updatedAt
    const ticketIndex = tickets.findIndex(t => t.id === params.id);
    if (ticketIndex !== -1) {
      tickets[ticketIndex].updatedAt = new Date().toISOString();
    }
    
    return HttpResponse.json(newMessage);
  }),

  http.patch('/api/tickets/:id', async ({ params, request }) => {
    const body = await request.json() as Partial<Pick<Ticket, 'status' | 'assignedStaffId'>>;
    const ticketIndex = tickets.findIndex(t => t.id === params.id);
    
    if (ticketIndex === -1) {
      return HttpResponse.json({ error: 'Ticket not found' }, { status: 404 });
    }
    
    tickets[ticketIndex] = {
      ...tickets[ticketIndex],
      ...body,
      updatedAt: new Date().toISOString(),
    };
    
    return HttpResponse.json(tickets[ticketIndex]);
  }),

  // Staff endpoints
  http.get('/api/staff', () => {
    const staff = mockUsers.filter(u => u.role === 'staff' || u.role === 'admin');
    return HttpResponse.json(staff);
  }),
];