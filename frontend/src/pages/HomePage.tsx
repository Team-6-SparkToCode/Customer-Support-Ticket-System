import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export function HomePage() {
  const { user } = useAuth();

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  // Redirect to appropriate home based on role
  const homeRoute = user.role === 'customer' ? '/my/tickets' : '/staff/tickets';
  return <Navigate to={homeRoute} replace />;
}