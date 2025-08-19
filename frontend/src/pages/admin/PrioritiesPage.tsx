import React from 'react';
import { Card, CardContent } from '../../components/ui/Card';
import { PriorityBadge } from '../../components/badges/PriorityBadge';
import { Priority } from '../../types';

const priorities: { name: Priority; level: number; description: string }[] = [
  {
    name: 'low',
    level: 1,
    description: 'Non-urgent issues that can be addressed in due course',
  },
  {
    name: 'medium',
    level: 2,
    description: 'Standard priority for most support requests',
  },
  {
    name: 'high',
    level: 3,
    description: 'Important issues that need prompt attention',
  },
  {
    name: 'urgent',
    level: 4,
    description: 'Critical issues requiring immediate response',
  },
];

export function PrioritiesPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-ink">Priorities</h1>
        <p className="text-muted mt-2">System-defined priority levels</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {priorities.map((priority) => (
          <Card key={priority.name}>
            <CardContent>
              <div className="flex items-start justify-between mb-4">
                <div>
                  <div className="flex items-center space-x-3 mb-2">
                    <PriorityBadge priority={priority.name} />
                    <span className="text-lg font-semibold text-ink capitalize">
                      {priority.name}
                    </span>
                  </div>
                  <p className="text-muted text-sm">{priority.description}</p>
                </div>
                <div className="text-right">
                  <p className="text-xs text-muted">Level</p>
                  <p className="text-2xl font-bold text-ink">{priority.level}</p>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      <Card>
        <CardContent>
          <div className="text-center py-8">
            <p className="text-muted">
              Priorities are system-defined and cannot be modified to ensure consistency across all tickets.
            </p>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}