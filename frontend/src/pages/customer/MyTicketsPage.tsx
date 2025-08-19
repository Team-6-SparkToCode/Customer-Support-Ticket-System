import React from 'react';
import { Link } from 'react-router-dom';
import { useTickets } from '../../hooks/useTickets';
import { useCategories } from '../../hooks/useCategories';
import { TicketTable } from '../../components/tickets/TicketTable';
import { FiltersBar } from '../../components/tickets/FiltersBar';
import { Button } from '../../components/ui/Button';
import { Plus } from 'lucide-react';

export function MyTicketsPage() {
  const [filters, setFilters] = React.useState({
    mine: 'true',
    status: 'all',
    category: 'all',
    priority: 'all',
    search: '',
  });

  const { data: tickets = [], isLoading } = useTickets(filters);
  const { data: categories = [] } = useCategories();

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-3xl font-bold text-ink">My Tickets</h1>
            <p className="text-muted mt-2">View and manage your support requests</p>
          </div>
        </div>
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-12 text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-4 text-muted">Loading your tickets...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-ink">My Tickets</h1>
          <p className="text-muted mt-2">View and manage your support requests</p>
        </div>
        <Link to="/my/tickets/new">
          <Button className="flex items-center space-x-2">
            <Plus className="h-4 w-4" />
            <span>New Ticket</span>
          </Button>
        </Link>
      </div>

      <FiltersBar
        filters={filters}
        onFiltersChange={setFilters}
        categories={categories}
      />

      <TicketTable 
        tickets={tickets} 
        showCustomer={false}
        basePath="/my/tickets" 
      />
    </div>
  );
}