import { useQuery } from '@tanstack/react-query';
import apiClient from './apiClient';

export interface Client {
  id: string;
  name: string;
  province?: string;
  country?: string;
  billingTierId: string;
  totalAum?: number;
  totalFee?: number;
  effectiveFeeRate?: number;
}

const clientService = {
  // Get all clients
  getAllClients: async () => {
    const { data } = await apiClient.get('/clients');
    return data;
  }
};

// React Query hook
export const useClients = () => {
  return useQuery({
    queryKey: ['clients'],
    queryFn: clientService.getAllClients
  });
};

export default clientService;