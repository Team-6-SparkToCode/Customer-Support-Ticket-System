import React from 'react';
import { Link } from 'react-router-dom';
import { TicketWithMessages } from '../../types';
import { StatusBadge } from '../badges/StatusBadge';
import { PriorityBadge } from '../badges/PriorityBadge';
import { formatRelativeTime } from '../../lib/utils';
import { ExternalLink } from 'lucide-react';

interface TicketTableProps {
  tickets: TicketWithMessages[];
  showCustomer?: boolean;
  basePath?: string;
}

export function TicketTable({ 
  tickets, 
  showCustomer = true, 
  basePath = '/my/tickets' 
}: TicketTableProps) {
  if (tickets.length === 0) {
    return (
      <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-12 text-center">
        <p className="text-muted text-lg">No tickets found</p>
        <p className="text-sm text-muted mt-2">
          Tickets will appear here when they are created
        </p>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
      <div className="overflow-x-auto">
        <table className="w-full">
          <thead className="bg-gray-50 border-b border-gray-200">
            <tr>
              <th className="sticky top-0 px-6 py-4 text-left text-sm font-semibold text-ink bg-gray-50">
                ID
              </th>
              <th className="sticky top-0 px-6 py-4 text-left text-sm font-semibold text-ink bg-gray-50">
                Subject
              </th>
              {showCustomer && (
                <th className="sticky top-0 px-6 py-4 text-left text-sm font-semibold text-ink bg-gray-50">
                  Customer
                </th>
              )}
              <th className="sticky top-0 px-6 py-4 text-left text-sm font-semibold text-ink bg-gray-50">
                Category
              </th>
              <th className="sticky top-0 px-6 py-4 text-left text-sm font-semibold text-ink bg-gray-50">
                Priority
              </th>
              <th className="sticky top-0 px-6 py-4 text-left text-sm font-semibold text-ink bg-gray-50">
                Status
              </th>
              <th className="sticky top-0 px-6 py-4 text-left text-sm font-semibold text-ink bg-gray-50">
                Updated
              </th>
              <th className="sticky top-0 px-6 py-4 text-left text-sm font-semibold text-ink bg-gray-50">
                Actions
              </th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            {tickets.map((ticket) => (
              <tr
                key={ticket.id}
                className="hover:bg-gray-50 transition-colors"
              >
                <td className="px-6 py-4 text-sm font-mono text-muted">
                  #{ticket.id}
                </td>
                <td className="px-6 py-4">
                  <div className="max-w-xs">
                    <p className="text-sm font-medium text-ink truncate">
                      {ticket.subject}
                    </p>
                    <p className="text-sm text-muted truncate">
                      {ticket.description}
                    </p>
                  </div>
                </td>
                {showCustomer && (
                  <td className="px-6 py-4 text-sm text-ink">
                    {ticket.customer?.name}
                  </td>
                )}
                <td className="px-6 py-4 text-sm text-muted">
                  {ticket.category?.name}
                </td>
                <td className="px-6 py-4">
                  <PriorityBadge priority={ticket.priority} />
                </td>
                <td className="px-6 py-4">
                  <StatusBadge status={ticket.status} />
                </td>
                <td className="px-6 py-4 text-sm text-muted">
                  {formatRelativeTime(ticket.updatedAt)}
                </td>
                <td className="px-6 py-4">
                  <Link
                    to={`${basePath}/${ticket.id}`}
                    className="inline-flex items-center space-x-1 text-blue-600 hover:text-blue-700"
                  >
                    <span className="text-sm font-medium">View</span>
                    <ExternalLink className="h-3 w-3" />
                  </Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}