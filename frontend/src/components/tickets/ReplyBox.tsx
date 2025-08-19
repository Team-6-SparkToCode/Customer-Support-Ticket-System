import React from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Button } from '../ui/Button';
import { Textarea } from '../ui/Textarea';
import { Send, Lock } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

const replySchema = z.object({
  message: z.string().min(1, 'Message is required').min(10, 'Message must be at least 10 characters'),
});

type ReplyForm = z.infer<typeof replySchema>;

interface ReplyBoxProps {
  onSubmit: (data: ReplyForm & { isInternal?: boolean }) => void;
  isLoading?: boolean;
  disabled?: boolean;
  allowInternal?: boolean;
}

export function ReplyBox({ 
  onSubmit, 
  isLoading = false, 
  disabled = false, 
  allowInternal = false 
}: ReplyBoxProps) {
  const { user } = useAuth();
  const [isInternal, setIsInternal] = React.useState(false);
  
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<ReplyForm>({
    resolver: zodResolver(replySchema),
  });

  const handleFormSubmit = (data: ReplyForm) => {
    onSubmit({ ...data, isInternal: allowInternal ? isInternal : false });
    reset();
    setIsInternal(false);
  };

  if (disabled) {
    return (
      <div className="bg-gray-50 rounded-2xl p-4 text-center">
        <p className="text-muted">This ticket is closed and cannot receive new messages.</p>
      </div>
    );
  }

  return (
    <form onSubmit={handleSubmit(handleFormSubmit)} className="space-y-4">
      <div className="bg-white rounded-2xl border border-gray-200 p-4">
        <Textarea
          {...register('message')}
          placeholder="Type your message here..."
          rows={4}
          error={errors.message?.message}
        />
        
        <div className="flex items-center justify-between mt-4">
          <div className="flex items-center space-x-4">
            {allowInternal && (
              <label className="flex items-center space-x-2 cursor-pointer">
                <input
                  type="checkbox"
                  checked={isInternal}
                  onChange={(e) => setIsInternal(e.target.checked)}
                  className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                />
                <div className="flex items-center space-x-1">
                  <Lock className="h-4 w-4 text-yellow-600" />
                  <span className="text-sm text-muted">Internal Note</span>
                </div>
              </label>
            )}
          </div>
          
          <Button
            type="submit"
            disabled={isLoading}
            className="flex items-center space-x-2"
          >
            <Send className="h-4 w-4" />
            <span>{isLoading ? 'Sending...' : 'Send'}</span>
          </Button>
        </div>
      </div>
    </form>
  );
}