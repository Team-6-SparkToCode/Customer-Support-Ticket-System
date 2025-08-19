import React from 'react';
import { useTickets, useUpdateTicket } from '../../hooks/useTickets';
import { useCategories } from '../../hooks/useCategories';
import { useStaff } from '../../hooks/useStaff';
import { TicketTable } from '../../components/tickets/TicketTable';
import { FiltersBar } from '../../components/tickets/FiltersBar';

export function StaffTicketsPage() {
  const [filters, setFilters] = React.useState({
    status: 'all',
    category: 'all',
    assignee: 'all',
    priority: 'all',
    search: '',
  });

  const { data: tickets = [], isLoading } = useTickets(filters);
  const { data: categories = [] } = useCategories();
  const { data: staff = [] } = useStaff();

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div>
          <h1 className="text-3xl font-bold text-ink">All Tickets</h1>
          <p className="text-muted mt-2">Manage customer support requests</p>
        </div>
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-12 text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-4 text-muted">Loading tickets...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-ink">All Tickets</h1>
        <p className="text-muted mt-2">Manage customer support requests</p>
      </div>

      <FiltersBar
        filters={filters}
        onFiltersChange={setFilters}
        categories={categories}
        staff={staff}
        showAssigneeFilter={true}
      />

      <TicketTable 
        tickets={tickets} 
        showCustomer={true}
        basePath="/staff/tickets" 
      />
    </div>
  );
}