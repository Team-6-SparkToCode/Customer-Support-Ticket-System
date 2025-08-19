import React from 'react';
import { Status } from '../../types';
import { cn } from '../../lib/utils';
import { Check } from 'lucide-react';

interface StatusStepperProps {
  currentStatus: Status;
  className?: string;
}

const statuses: Status[] = ['open', 'in_progress', 'resolved', 'closed'];
const statusLabels: Record<Status, string> = {
  open: 'Open',
  in_progress: 'In Progress',
  resolved: 'Resolved',
  closed: 'Closed',
};

export function StatusStepper({ currentStatus, className }: StatusStepperProps) {
  const currentIndex = statuses.indexOf(currentStatus);

  return (
    <div className={cn('flex items-center space-x-4', className)}>
      {statuses.map((status, index) => {
        const isActive = index <= currentIndex;
        const isCurrent = index === currentIndex;
        
        return (
          <React.Fragment key={status}>
            <div className="flex items-center space-x-2">
              <div
                className={cn(
                  'flex items-center justify-center w-8 h-8 rounded-full border-2 transition-colors',
                  {
                    'bg-blue-600 border-blue-600 text-white': isActive,
                    'border-gray-300 text-gray-400': !isActive,
                    'ring-2 ring-blue-200': isCurrent && isActive,
                  }
                )}
              >
                {isActive ? (
                  <Check className="h-4 w-4" />
                ) : (
                  <span className="text-xs font-medium">{index + 1}</span>
                )}
              </div>
              <span
                className={cn(
                  'text-sm font-medium',
                  isActive ? 'text-ink' : 'text-muted'
                )}
              >
                {statusLabels[status]}
              </span>
            </div>
            
            {index < statuses.length - 1 && (
              <div
                className={cn(
                  'h-0.5 w-8 transition-colors',
                  index < currentIndex ? 'bg-blue-600' : 'bg-gray-300'
                )}
              />
            )}
          </React.Fragment>
        );
      })}
    </div>
  );
}