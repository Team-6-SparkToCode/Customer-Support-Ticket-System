import React from 'react';
import { Priority } from '../../types';
import { cn } from '../../lib/utils';

interface PriorityBadgeProps {
  priority: Priority;
  className?: string;
}

export function PriorityBadge({ priority, className }: PriorityBadgeProps) {
  return (
    <span
      className={cn(
        'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
        {
          'bg-gray-100 text-gray-700': priority === 'low',
          'bg-yellow-100 text-yellow-800': priority === 'medium',
          'bg-orange-100 text-orange-800': priority === 'high',
          'bg-red-100 text-red-800': priority === 'urgent',
        },
        className
      )}
    >
      {priority.charAt(0).toUpperCase() + priority.slice(1)}
    </span>
  );
}