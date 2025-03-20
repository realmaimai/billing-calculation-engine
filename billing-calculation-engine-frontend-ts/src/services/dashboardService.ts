import { useQuery } from '@tanstack/react-query';
import apiClient from './apiClient';

export interface DashboardSummary {
  totalClients?: number;
  totalPortfolios?: number;
  totalAssets?: number;
}

const dashboardService = {
  // Get dashboard summary data
  getSummary: async () => {
    const { data } = await apiClient.get('/dashboard/summary');
    return data;
  }
};

export const useDashboardSummary = () => {
  return useQuery({
    queryKey: ['dashboard', 'summary'],
    queryFn: dashboardService.getSummary
  });
};

export default dashboardService;