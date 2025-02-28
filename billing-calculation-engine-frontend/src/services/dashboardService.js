import api from './api';

/**
 * Simple service for dashboard API operations
 */
const dashboardService = {
  /**
   * Get dashboard summary data
   * @returns {Promise} - Promise that resolves to dashboard summary data
   */
  getSummary: () => {
    return api.get('/dashboard/summary');
  }
};

export default dashboardService;