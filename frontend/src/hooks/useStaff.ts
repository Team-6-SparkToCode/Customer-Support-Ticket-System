import { useQuery } from '@tanstack/react-query';
import { User } from '../types';

const API_BASE = '/api';

export function useStaff() {
  return useQuery<User[]>({
    queryKey: ['staff'],
    queryFn: async () => {
      const response = await fetch(`${API_BASE}/staff`);
      if (!response.ok) throw new Error('Failed to fetch staff');
      return response.json();
    },
  });
}