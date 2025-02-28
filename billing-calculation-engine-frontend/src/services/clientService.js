import api from './api';

/**
 * Simple service for client API operations
 */
const clientService = {
  /**
   * Get all clients
   * @returns {Promise} - Promise that resolves to client data
   */
  getAllClients: () => {
    return api.get('/clients');
  }
};

export default clientService;