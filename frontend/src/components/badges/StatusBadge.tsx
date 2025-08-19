import React from 'react';
import { Status } from '../../types';
import { cn } from '../../lib/utils';

interface StatusBadgeProps {
  status: Status;
  className?: string;
}

export function StatusBadge({ status, className }: StatusBadgeProps) {
  return (
    <span
      className={cn(
        'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
        {
          'bg-yellow-100 text-yellow-800': status === 'open',
          'bg-blue-100 text-blue-800': status === 'in_progress',
          'bg-green-100 text-green-800': status === 'resolved',
          'bg-gray-100 text-gray-800': status === 'closed',
        },
        className
      )}
    >
      {status.replace('_', ' ').replace(/\b\w/g, l => l.toUpperCase())}
    </span>
  );
}