import React from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useCategories } from '../../hooks/useCategories';
import { useCreateTicket } from '../../hooks/useTickets';
import { Card, CardHeader, CardContent } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Textarea } from '../../components/ui/Textarea';
import { Select } from '../../components/ui/Select';
import { ArrowLeft } from 'lucide-react';
import toast from 'react-hot-toast';

const ticketSchema = z.object({
  categoryId: z.string().min(1, 'Category is required'),
  priority: z.enum(['low', 'medium', 'high', 'urgent']),
  subject: z.string().min(1, 'Subject is required').min(5, 'Subject must be at least 5 characters'),
  description: z.string().min(1, 'Description is required').min(20, 'Description must be at least 20 characters'),
});

type TicketForm = z.infer<typeof ticketSchema>;

export function NewTicketPage() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const { data: categories = [] } = useCategories();
  const createTicket = useCreateTicket();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<TicketForm>({
    resolver: zodResolver(ticketSchema),
    defaultValues: {
      priority: 'medium',
    },
  });

  const onSubmit = async (data: TicketForm) => {
    if (!user) return;
    
    try {
      const ticket = await createTicket.mutateAsync({
        ...data,
        customerId: user.id,
      });
      
      toast.success('Ticket created successfully!');
      navigate(`/my/tickets/${ticket.id}`);
    } catch (error) {
      toast.error('Failed to create ticket. Please try again.');
    }
  };

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <div className="flex items-center space-x-4">
        <Button
          variant="ghost"
          onClick={() => navigate('/my/tickets')}
          className="flex items-center space-x-2"
        >
          <ArrowLeft className="h-4 w-4" />
          <span>Back to Tickets</span>
        </Button>
      </div>

      <div>
        <h1 className="text-3xl font-bold text-ink">Create New Ticket</h1>
        <p className="text-muted mt-2">Describe your issue and we'll help you resolve it</p>
      </div>

      <Card>
        <CardContent>
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <Select
                {...register('categoryId')}
                label="Category"
                error={errors.categoryId?.message}
              >
                <option value="">Select a category</option>
                {categories.map((category) => (
                  <option key={category.id} value={category.id}>
                    {category.name}
                  </option>
                ))}
              </Select>

              <Select
                {...register('priority')}
                label="Priority"
                error={errors.priority?.message}
              >
                <option value="low">Low</option>
                <option value="medium">Medium</option>
                <option value="high">High</option>
                <option value="urgent">Urgent</option>
              </Select>
            </div>

            <Input
              {...register('subject')}
              label="Subject"
              placeholder="Brief description of your issue"
              error={errors.subject?.message}
            />

            <Textarea
              {...register('description')}
              label="Description"
              placeholder="Please provide detailed information about your issue..."
              rows={6}
              error={errors.description?.message}
            />

            <div className="flex justify-end space-x-4">
              <Button
                type="button"
                variant="outline"
                onClick={() => navigate('/my/tickets')}
              >
                Cancel
              </Button>
              <Button
                type="submit"
                disabled={createTicket.isPending}
              >
                {createTicket.isPending ? 'Creating...' : 'Create Ticket'}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}