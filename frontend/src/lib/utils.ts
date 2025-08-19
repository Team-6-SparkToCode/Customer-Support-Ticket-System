import { type ClassValue, clsx } from 'clsx';

export function cn(...inputs: ClassValue[]) {
  return clsx(inputs);
}

export function formatDate(date: string | Date) {
  return new Intl.DateTimeFormat('en-US', {
    month: 'short',
    day: 'numeric',
    year: 'numeric',
    hour: 'numeric',
    minute: '2-digit',
  }).format(new Date(date));
}

export function formatRelativeTime(date: string | Date) {
  const now = new Date();
  const target = new Date(date);
  const diffInHours = Math.abs(now.getTime() - target.getTime()) / (1000 * 60 * 60);
  
  if (diffInHours < 24) {
    return new Intl.RelativeTimeFormat('en', { numeric: 'auto' }).format(
      -Math.floor(diffInHours),
      'hour'
    );
  }
  
  const diffInDays = Math.floor(diffInHours / 24);
  return new Intl.RelativeTimeFormat('en', { numeric: 'auto' }).format(
    -diffInDays,
    'day'
  );
}