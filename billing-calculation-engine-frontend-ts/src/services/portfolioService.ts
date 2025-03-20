import { useQuery } from '@tanstack/react-query';
import apiClient from './apiClient';

export interface Portfolio {
  portfolioId: string;
  clientId: string;
  portfolioCurrency: 'CAD' | 'USD';
  portfolioAum: number;
  portfolioFee: number;
}


const portfolioService = {
  // Get all portfolios
  getAllPortfolios: async () => {
    const { data } = await apiClient.get('/portfolios');
    return data;
  }
};

export const usePortfolios = () => {
  return useQuery({
    queryKey: ['portfolios'],
    queryFn: portfolioService.getAllPortfolios
  });
};

export default portfolioService;