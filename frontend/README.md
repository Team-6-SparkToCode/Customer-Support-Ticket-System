# Customer Support Ticket System

A comprehensive customer support ticket system built with React, TypeScript, and Tailwind CSS. Features role-based access control, real-time ticket management, and a complete admin interface.

## Features

### 🎯 Core Functionality
- **Role-based Authentication** (Customer, Staff, Admin)
- **Complete Ticket Lifecycle** (Open → In Progress → Resolved → Closed)
- **Real-time Filtering & Search** across all ticket attributes
- **Threaded Conversations** with attachment support
- **Internal Staff Notes** (visible only to staff/admin)
- **Assignment Management** for staff members
- **Admin CRUD Operations** for categories, priorities, and staff

### 🛠 Technical Features
- **Mock Service Worker (MSW)** for API simulation
- **React Query** for efficient data fetching and caching
- **React Hook Form + Zod** for robust form validation
- **TypeScript** for type safety throughout
- **Responsive Design** with Tailwind CSS
- **Toast Notifications** for user feedback
- **Protected Routes** with role-based access control

### 👥 User Roles

#### Customer
- Submit new support tickets
- View and manage their own tickets
- Participate in ticket conversations
- Track ticket status and updates

#### Staff
- View and manage all tickets
- Assign tickets to team members
- Update ticket status with enforced workflow
- Add internal notes for team communication
- Access comprehensive dashboard with metrics

#### Admin
- All staff capabilities
- Manage support categories
- Configure priority levels
- Add/edit/remove staff members
- Full system administration

## Getting Started

### Prerequisites
- Node.js 18+ 
- npm or yarn

### Installation

1. **Clone and install dependencies**
   ```bash
   git clone <repository-url>
   cd customer-support-system
   npm install
   ```

2. **Start the development server**
   ```bash
   npm run dev
   ```

3. **Open your browser**
   Navigate to `http://localhost:5173`

## Demo Accounts

The system includes pre-configured demo accounts for testing:

### Customer Account
- **Email:** `customer@example.com`
- **Role:** Customer
- **Access:** Personal tickets only

### Staff Accounts
- **Email:** `staff1@example.com` (Sarah Support)
- **Role:** Staff
- **Department:** Technical Support

- **Email:** `staff2@example.com` (Mike Manager)  
- **Role:** Staff
- **Department:** Customer Success

### Admin Account
- **Email:** `admin@example.com`
- **Role:** Admin
- **Access:** Full system administration

### Login Instructions
1. Go to the login page
2. Click on any demo account button to auto-fill credentials
3. Select the corresponding role from the dropdown
4. Click "Sign in"

## Status Workflow

The system enforces a strict ticket status workflow:

```
Open → In Progress → Resolved → Closed
                              ↓
                     Reopen (→ In Progress)
```

### Status Rules
- **Open:** New tickets start here
- **In Progress:** Staff can move open tickets here
- **Resolved:** Staff can resolve in-progress tickets
- **Closed:** Final state for resolved tickets
- **Reopen:** Closed tickets can be reopened (returns to In Progress)

## Project Structure

```
src/
├── components/          # Reusable UI components
│   ├── admin/          # Admin-specific components
│   ├── layout/         # Layout components (AppShell)
│   ├── tickets/        # Ticket-related components
│   └── ui/             # Generic UI components
├── context/            # React context providers
├── hooks/              # Custom React hooks
├── mocks/              # MSW mock data and handlers
├── pages/              # Page components organized by role
├── types/              # TypeScript type definitions
└── utils/              # Utility functions
```

## Key Technologies

- **React 18** - Modern React with hooks and concurrent features
- **TypeScript** - Full type safety and developer experience
- **Vite** - Fast development and build tool
- **Tailwind CSS** - Utility-first CSS framework
- **React Router** - Client-side routing with protected routes
- **React Query** - Server state management and caching
- **React Hook Form** - Performant form library
- **Zod** - Schema validation
- **MSW** - API mocking for development
- **Lucide React** - Beautiful icon library
- **Headless UI** - Accessible component primitives

## Development

### Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint

### Adding New Features

1. **Define Types** - Add to `src/types/index.ts`
2. **Create API Handlers** - Update `src/mocks/handlers.ts`
3. **Add Hooks** - Create custom hooks in `src/hooks/`
4. **Build Components** - Add to appropriate component directory
5. **Create Pages** - Add to `src/pages/` with proper routing
6. **Update Navigation** - Modify `src/components/layout/AppShell.tsx`

## Architecture Highlights

### State Management
- **Global State:** React Context for auth and toasts
- **Server State:** React Query for all API interactions
- **Form State:** React Hook Form for complex forms

### Authentication
- **Client-only** authentication using localStorage
- **Role-based** route protection
- **Automatic redirects** based on user role

### API Design
- **RESTful** endpoints with consistent patterns
- **Type-safe** request/response handling
- **Optimistic updates** for better UX

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.