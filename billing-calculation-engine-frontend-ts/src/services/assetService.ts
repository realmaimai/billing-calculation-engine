import { useQuery } from '@tanstack/react-query';
import apiClient from './apiClient';

export interface Asset {
  id: string;
  name: string;
  portfolioId: string;
}

const assetService = {
  // Get assets for a specific portfolio
  getPortfolioAssets: async (portfolioId: string) => {
    const { data } = await apiClient.get(`/assets/${portfolioId}`);
    return data;
  }
};

// React Query hook
export const usePortfolioAssets = (portfolioId: string) => {
  return useQuery({
    queryKey: ['assets', portfolioId],
    queryFn: () => assetService.getPortfolioAssets(portfolioId),
    enabled: !!portfolioId
  });
};

export default assetService;