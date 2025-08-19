import React from 'react';
import { Role } from '../../types';
import { cn } from '../../lib/utils';

interface RoleBadgeProps {
  role: Role;
  className?: string;
}

export function RoleBadge({ role, className }: RoleBadgeProps) {
  return (
    <span
      className={cn(
        'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
        {
          'bg-blue-100 text-blue-800': role === 'customer',
          'bg-green-100 text-green-800': role === 'staff',
          'bg-purple-100 text-purple-800': role === 'admin',
        },
        className
      )}
    >
      {role.charAt(0).toUpperCase() + role.slice(1)}
    </span>
  );
}