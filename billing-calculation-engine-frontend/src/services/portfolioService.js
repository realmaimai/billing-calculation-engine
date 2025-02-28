import api from './api';

/**
 * Simple service for portfolio API operations
 */
const portfolioService = {
  /**
   * Get all portfolios
   * @returns {Promise} - Promise that resolves to portfolio data
   */
  getAllPortfolios: () => {
    return api.get('/portfolios');
  }
};

export default portfolioService;