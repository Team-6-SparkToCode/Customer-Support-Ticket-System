import React from 'react';
import { useStaff } from '../../hooks/useStaff';
import { Card, CardHeader, CardContent } from '../../components/ui/Card';
import { RoleBadge } from '../../components/badges/RoleBadge';
import { User, Mail, Building } from 'lucide-react';

export function StaffPage() {
  const { data: staff = [], isLoading } = useStaff();

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div>
          <h1 className="text-3xl font-bold text-ink">Staff Management</h1>
          <p className="text-muted mt-2">View and manage staff members</p>
        </div>
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-12 text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-4 text-muted">Loading staff...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-ink">Staff Management</h1>
        <p className="text-muted mt-2">View and manage staff members</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {staff.map((member) => (
          <Card key={member.id}>
            <CardContent>
              <div className="flex items-center space-x-4">
                <div className="bg-blue-100 rounded-full p-3">
                  <User className="h-6 w-6 text-blue-600" />
                </div>
                <div className="flex-1">
                  <div className="flex items-center space-x-2 mb-1">
                    <h3 className="text-lg font-semibold text-ink">{member.name}</h3>
                    <RoleBadge role={member.role} />
                  </div>
                  
                  <div className="space-y-2">
                    <div className="flex items-center space-x-2">
                      <Mail className="h-4 w-4 text-muted" />
                      <span className="text-sm text-muted">{member.email}</span>
                    </div>
                    
                    {member.department && (
                      <div className="flex items-center space-x-2">
                        <Building className="h-4 w-4 text-muted" />
                        <span className="text-sm text-muted">{member.department}</span>
                      </div>
                    )}
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {staff.length === 0 && (
        <Card>
          <CardContent>
            <div className="text-center py-12">
              <p className="text-muted text-lg">No staff members found</p>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  );
}