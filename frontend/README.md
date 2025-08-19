# Customer Support Ticket System

A comprehensive frontend-only customer support ticket management system built with modern React technologies.

## 🛠️ Tech Stack

- **Framework**: React 18 with TypeScript
- **Build Tool**: Vite
- **Styling**: Tailwind CSS
- **Routing**: React Router v6
- **State Management**: TanStack Query (React Query)
- **Forms**: React Hook Form + Zod validation
- **API Mocking**: MSW (Mock Service Worker)
- **Notifications**: React Hot Toast
- **Icons**: Lucide React

## 🚀 Features

### Authentication & Authorization
- Role-based authentication (Customer, Staff, Admin)
- Protected routes with automatic redirection
- Persistent login state with localStorage

### Customer Features
- Submit support tickets with categories and priorities
- View personal ticket history with filters and search
- Track ticket status through visual stepper
- Threaded conversation view with real-time updates
- File attachment support (mock implementation)

### Staff Features
- View and manage all customer tickets
- Advanced filtering (status, category, assignee, priority)
- Ticket assignment and status management
- Internal notes system (staff-only communication)
- Comprehensive dashboard with statistics

### Admin Features
- Complete staff management interface
- Category and priority configuration
- System-wide ticket oversight
- User role management

## 🎨 Design System

### Colors
- **Background**: #E6E6E6 (Light gray)
- **Ink**: #29353C (Dark blue-gray)
- **Muted**: #768A96 (Medium gray)
- **Primary**: Blue (#3B82F6)
- **Secondary**: Teal (#14B8A6)
- **Accent**: Orange (#F97316)

### Components
- Minimalist cards with soft shadows and rounded-2xl borders
- Consistent spacing system (8px base)
- Typography hierarchy with varied font sizes
- Status badges with semantic colors
- Interactive elements with hover states and micro-interactions

## 📱 User Roles & Access

### Customer (`customer`)
- **Routes**: `/my/tickets`, `/my/tickets/new`, `/my/tickets/:id`
- **Demo Account**: john@customer.com / password

### Staff (`staff`)
- **Routes**: `/staff/tickets`, `/staff/tickets/:id`, `/staff/dashboard`
- **Demo Account**: sarah@company.com / password

### Admin (`admin`)
- **Routes**: All staff routes + `/admin/categories`, `/admin/priorities`, `/admin/staff`
- **Demo Account**: admin@company.com / password

## 🛠️ Development

### Prerequisites
- Node.js 18+ 
- npm or yarn

### Installation
```bash
# Install dependencies
npm install

# Start development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

### Scripts
- `npm run dev` - Start development server with hot reload
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint

## 🔧 How to Switch User Roles

1. **Login Page**: Use the role dropdown to select different user types
2. **Demo Accounts**:
   - Customer: `john@customer.com`
   - Staff: `sarah@company.com` or `mike@company.com`
   - Admin: `admin@company.com`
   - Password for all accounts: `password`

3. **Testing Different Flows**:
   - Log in as Customer to create and track tickets
   - Switch to Staff to manage tickets and assign priorities
   - Use Admin account to configure system settings

## 📊 Mock Data

The system includes comprehensive seed data:
- **3 Categories**: Technical Issue, Billing, General Inquiry
- **4 Priority Levels**: Low, Medium, High, Urgent
- **4 Demo Users**: 1 Customer, 2 Staff, 1 Admin
- **12+ Sample Tickets** with threaded conversations
- **Realistic Timestamps** and status progression

## 🔄 Status Workflow

Tickets follow a strict status progression:
1. **Open** → New tickets start here
2. **In Progress** → Staff actively working
3. **Resolved** → Issue fixed, awaiting customer confirmation
4. **Closed** → Fully resolved and archived

The StatusStepper component visually represents this progression and prevents invalid status transitions.

## 🔒 Security Features

- Client-side route protection
- Role-based component rendering
- Input validation with Zod schemas
- XSS protection through proper React practices
- CSRF protection simulation in MSW handlers

## 📱 Responsive Design

- Mobile-first approach with Tailwind breakpoints
- Adaptive layouts for phone, tablet, and desktop
- Touch-friendly interface elements
- Optimized for screens from 320px to 1920px+

## 🎯 Production Readiness

While this is a frontend-only demo, it includes production-level features:
- Comprehensive error handling
- Loading states and skeleton screens
- Optimistic updates with query invalidation
- Accessibility compliance (WCAG guidelines)
- SEO-friendly structure
- Performance optimization with code splitting

## 🚀 Deployment

The application is ready for deployment to any static hosting service:
- Netlify
- Vercel
- GitHub Pages
- AWS S3 + CloudFront

Simply run `npm run build` and deploy the `dist` folder.

---

Built with ❤️ using React, TypeScript, and modern web technologies.