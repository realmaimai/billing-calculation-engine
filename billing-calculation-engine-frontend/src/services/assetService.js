// src/services/assetService.js
import api from './api';

/**
 * Simple service for asset API operations
 */
const assetService = {
  /**
   * Get all assets for a specific portfolio
   * @param {string} portfolioId - The portfolio ID
   * @returns {Promise} - Promise that resolves to assets data
   */
  getPortfolioAssets: (portfolioId) => {
    return api.get(`/assets/${portfolioId}`);
  }
};

export default assetService;