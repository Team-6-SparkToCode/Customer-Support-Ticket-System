import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from './context/AuthContext';
import { AppShell } from './components/layout/AppShell';
import { ProtectedRoute } from './components/layout/ProtectedRoute';
import { LoginPage } from './pages/LoginPage';
import { HomePage } from './pages/HomePage';

// Customer pages
import { MyTicketsPage } from './pages/customer/MyTicketsPage';
import { NewTicketPage } from './pages/customer/NewTicketPage';
import { TicketDetailPage } from './pages/customer/TicketDetailPage';

// Staff pages
import { StaffTicketsPage } from './pages/staff/StaffTicketsPage';
import { StaffTicketDetailPage } from './pages/staff/StaffTicketDetailPage';
import { StaffDashboardPage } from './pages/staff/StaffDashboardPage';

// Admin pages
import { CategoriesPage } from './pages/admin/CategoriesPage';
import { PrioritiesPage } from './pages/admin/PrioritiesPage';
import { StaffPage } from './pages/admin/StaffPage';

// Initialize MSW
if (process.env.NODE_ENV === 'development') {
  import('./mocks/browser').then(({ worker }) => {
    worker.start({
      onUnhandledRequest: 'bypass',
    });
  });
}

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      staleTime: 5 * 60 * 1000, // 5 minutes
    },
  },
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <BrowserRouter>
          <div className="min-h-screen bg-bg">
            <Routes>
              <Route path="/login" element={<LoginPage />} />
              
              <Route path="/" element={
                <ProtectedRoute>
                  <HomePage />
                </ProtectedRoute>
              } />

              <Route path="/*" element={
                <ProtectedRoute>
                  <AppShell />
                </ProtectedRoute>
              }>
                {/* Customer Routes */}
                <Route path="my/tickets" element={
                  <ProtectedRoute allowedRoles={['customer']}>
                    <MyTicketsPage />
                  </ProtectedRoute>
                } />
                <Route path="my/tickets/new" element={
                  <ProtectedRoute allowedRoles={['customer']}>
                    <NewTicketPage />
                  </ProtectedRoute>
                } />
                <Route path="my/tickets/:id" element={
                  <ProtectedRoute allowedRoles={['customer']}>
                    <TicketDetailPage />
                  </ProtectedRoute>
                } />

                {/* Staff Routes */}
                <Route path="staff/tickets" element={
                  <ProtectedRoute allowedRoles={['staff', 'admin']}>
                    <StaffTicketsPage />
                  </ProtectedRoute>
                } />
                <Route path="staff/tickets/:id" element={
                  <ProtectedRoute allowedRoles={['staff', 'admin']}>
                    <StaffTicketDetailPage />
                  </ProtectedRoute>
                } />
                <Route path="staff/dashboard" element={
                  <ProtectedRoute allowedRoles={['staff', 'admin']}>
                    <StaffDashboardPage />
                  </ProtectedRoute>
                } />

                {/* Admin Routes */}
                <Route path="admin/categories" element={
                  <ProtectedRoute allowedRoles={['admin']}>
                    <CategoriesPage />
                  </ProtectedRoute>
                } />
                <Route path="admin/priorities" element={
                  <ProtectedRoute allowedRoles={['admin']}>
                    <PrioritiesPage />
                  </ProtectedRoute>
                } />
                <Route path="admin/staff" element={
                  <ProtectedRoute allowedRoles={['admin']}>
                    <StaffPage />
                  </ProtectedRoute>
                } />
              </Route>
            </Routes>
          </div>
          
          <Toaster
            position="top-right"
            toastOptions={{
              duration: 4000,
              style: {
                background: 'white',
                color: '#29353C',
                borderRadius: '16px',
                boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)',
              },
              success: {
                iconTheme: {
                  primary: '#10B981',
                  secondary: 'white',
                },
              },
              error: {
                iconTheme: {
                  primary: '#EF4444',
                  secondary: 'white',
                },
              },
            }}
          />
        </BrowserRouter>
      </AuthProvider>
    </QueryClientProvider>
  );
}

export default App;