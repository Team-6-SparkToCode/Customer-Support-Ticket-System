import React from 'react';
import { Select } from '../ui/Select';
import { Input } from '../ui/Input';
import { Search } from 'lucide-react';
import { Status, Priority, Category, User } from '../../types';

interface FiltersBarProps {
  filters: {
    status?: string;
    category?: string;
    assignee?: string;
    priority?: string;
    search?: string;
  };
  onFiltersChange: (filters: any) => void;
  categories: Category[];
  staff?: User[];
  showAssigneeFilter?: boolean;
}

export function FiltersBar({
  filters,
  onFiltersChange,
  categories,
  staff = [],
  showAssigneeFilter = false,
}: FiltersBarProps) {
  const handleFilterChange = (key: string, value: string) => {
    onFiltersChange({ ...filters, [key]: value });
  };

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-6">
      <div className="grid grid-cols-1 md:grid-cols-5 gap-4">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted" />
          <Input
            placeholder="Search tickets..."
            value={filters.search || ''}
            onChange={(e) => handleFilterChange('search', e.target.value)}
            className="pl-10"
          />
        </div>

        <Select
          value={filters.status || 'all'}
          onChange={(e) => handleFilterChange('status', e.target.value)}
        >
          <option value="all">All Statuses</option>
          <option value="open">Open</option>
          <option value="in_progress">In Progress</option>
          <option value="resolved">Resolved</option>
          <option value="closed">Closed</option>
        </Select>

        <Select
          value={filters.category || 'all'}
          onChange={(e) => handleFilterChange('category', e.target.value)}
        >
          <option value="all">All Categories</option>
          {categories.map((category) => (
            <option key={category.id} value={category.id}>
              {category.name}
            </option>
          ))}
        </Select>

        <Select
          value={filters.priority || 'all'}
          onChange={(e) => handleFilterChange('priority', e.target.value)}
        >
          <option value="all">All Priorities</option>
          <option value="low">Low</option>
          <option value="medium">Medium</option>
          <option value="high">High</option>
          <option value="urgent">Urgent</option>
        </Select>

        {showAssigneeFilter && (
          <Select
            value={filters.assignee || 'all'}
            onChange={(e) => handleFilterChange('assignee', e.target.value)}
          >
            <option value="all">All Assignees</option>
            <option value="">Unassigned</option>
            {staff.map((member) => (
              <option key={member.id} value={member.id}>
                {member.name}
              </option>
            ))}
          </Select>
        )}
      </div>
    </div>
  );
}