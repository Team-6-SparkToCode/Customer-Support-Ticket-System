import { User, Category, PriorityDef, Ticket, TicketMessage } from '../types';

export const mockUsers: User[] = [
  {
    id: '1',
    name: 'Mohammed Customer',
    email: 'Mohammed@customer.com',
    phone: '+968 95963837',
    role: 'customer',
  },
  {
    id: '2',
    name: 'Ahmed Staff',
    email: 'Ahmed@company.com',
    role: 'staff',
    department: 'Technical Support',
  },
  {
    id: '3',
    name: 'Khalid Staff',
    email: 'Khalid@company.com',
    role: 'staff',
    department: 'Billing',
  },
  {
    id: '4',
    name: 'Abdulaziz Staff',
    email: 'Abdulaziz@company.com',
    role: 'admin',
    department: 'Management',
  },
];

export const mockCategories: Category[] = [
  {
    id: '1',
    name: 'Technical Issue',
    description: 'Problems with software, hardware, or technical functionality',
  },
  {
    id: '2',
    name: 'Billing',
    description: 'Questions about invoices, payments, and account charges',
  },
  {
    id: '3',
    name: 'General Inquiry',
    description: 'General questions and information requests',
  },
];

export const mockPriorities: PriorityDef[] = [
  { id: '1', name: 'low', level: 1 },
  { id: '2', name: 'medium', level: 2 },
  { id: '3', name: 'high', level: 3 },
  { id: '4', name: 'urgent', level: 4 },
];

export const mockTickets: Ticket[] = [
  {
    id: '1',
    customerId: '1',
    assignedStaffId: '2',
    categoryId: '1',
    priority: 'high',
    subject: 'Login issues with mobile app',
    description: 'Unable to login to the mobile application. Keep getting "invalid credentials" error.',
    status: 'in_progress',
    createdAt: '2024-01-15T10:30:00Z',
    updatedAt: '2024-01-15T14:20:00Z',
  },
  {
    id: '2',
    customerId: '1',
    assignedStaffId: '3',
    categoryId: '2',
    priority: 'medium',
    subject: 'Billing inquiry about December charges',
    description: 'I see some unexpected charges on my December bill. Can you help clarify?',
    status: 'resolved',
    createdAt: '2024-01-14T09:15:00Z',
    updatedAt: '2024-01-14T16:45:00Z',
  },
  {
    id: '3',
    customerId: '1',
    categoryId: '3',
    priority: 'low',
    subject: 'Question about new features',
    description: 'When will the new dashboard features be available?',
    status: 'open',
    createdAt: '2024-01-16T11:20:00Z',
    updatedAt: '2024-01-16T11:20:00Z',
  },
];

export const mockTicketMessages: TicketMessage[] = [
  {
    id: '1',
    ticketId: '1',
    senderId: '1',
    message: 'Unable to login to the mobile application. Keep getting "invalid credentials" error.',
    createdAt: '2024-01-15T10:30:00Z',
  },
  {
    id: '2',
    ticketId: '1',
    senderId: '2',
    message: 'Thanks for reaching out. I can help you with this login issue. Can you try clearing your app cache and attempting to login again?',
    createdAt: '2024-01-15T11:45:00Z',
  },
  {
    id: '3',
    ticketId: '1',
    senderId: '1',
    message: 'I tried clearing the cache but still having the same issue. The error persists.',
    createdAt: '2024-01-15T12:30:00Z',
  },
  {
    id: '4',
    ticketId: '1',
    senderId: '2',
    message: 'Let me escalate this to our development team. In the meantime, please try using the web version.',
    createdAt: '2024-01-15T14:20:00Z',
    isInternal: true,
  },
  {
    id: '5',
    ticketId: '2',
    senderId: '1',
    message: 'I see some unexpected charges on my December bill. Can you help clarify?',
    createdAt: '2024-01-14T09:15:00Z',
  },
  {
    id: '6',
    ticketId: '2',
    senderId: '3',
    message: 'I\'ve reviewed your account and those charges are for the premium features you activated in November. I\'ll send you a detailed breakdown via email.',
    createdAt: '2024-01-14T10:30:00Z',
  },
  {
    id: '7',
    ticketId: '2',
    senderId: '1',
    message: 'Perfect, that makes sense now. Thank you for the clarification!',
    createdAt: '2024-01-14T16:45:00Z',
  },
  {
    id: '8',
    ticketId: '3',
    senderId: '1',
    message: 'When will the new dashboard features be available?',
    createdAt: '2024-01-16T11:20:00Z',
  },
];