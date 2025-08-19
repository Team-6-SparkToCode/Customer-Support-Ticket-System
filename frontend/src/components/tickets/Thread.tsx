import React from 'react';
import { TicketMessage, User } from '../../types';
import { formatDate } from '../../lib/utils';
import { cn } from '../../lib/utils';
import { Shield } from 'lucide-react';

interface ThreadProps {
  messages: TicketMessage[];
  users: User[];
  currentUserId?: string;
}

export function Thread({ messages, users, currentUserId }: ThreadProps) {
  const getUserById = (id: string) => users.find(u => u.id === id);

  return (
    <div className="space-y-6">
      {messages.map((message) => {
        const author = getUserById(message.senderId);
        const isCurrentUser = message.senderId === currentUserId;
        const isInternal = message.isInternal;

        return (
          <div
            key={message.id}
            className={cn(
              'flex',
              isCurrentUser ? 'justify-end' : 'justify-start'
            )}
          >
            <div
              className={cn(
                'max-w-2xl rounded-2xl px-4 py-3 shadow-sm',
                isCurrentUser
                  ? 'bg-blue-600 text-white'
                  : isInternal
                  ? 'bg-yellow-50 border border-yellow-200 text-gray-900'
                  : 'bg-white border border-gray-200 text-gray-900'
              )}
            >
              <div className="flex items-center justify-between mb-2">
                <div className="flex items-center space-x-2">
                  <span
                    className={cn(
                      'text-sm font-medium',
                      isCurrentUser ? 'text-blue-100' : 'text-muted'
                    )}
                  >
                    {author?.name || 'Unknown User'}
                  </span>
                  {isInternal && (
                    <div className="flex items-center space-x-1">
                      <Shield className="h-3 w-3 text-yellow-600" />
                      <span className="text-xs text-yellow-600 font-medium">
                        Internal
                      </span>
                    </div>
                  )}
                </div>
                <span
                  className={cn(
                    'text-xs',
                    isCurrentUser ? 'text-blue-200' : 'text-muted'
                  )}
                >
                  {formatDate(message.createdAt)}
                </span>
              </div>
              
              <p
                className={cn(
                  'text-sm leading-relaxed whitespace-pre-wrap',
                  isCurrentUser ? 'text-white' : 'text-gray-900'
                )}
              >
                {message.message}
              </p>
            </div>
          </div>
        );
      })}
    </div>
  );
}