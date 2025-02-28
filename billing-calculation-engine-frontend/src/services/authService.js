import api from './api';

/**
 * Service for authentication-related API operations
 */
const authService = {
  /**
   * Attempt to login a user
   * @param {Object} credentials - User credentials (email, password)
   * @returns {Promise} - Promise that resolves to login response with token
   */
  login: async (credentials) => {
    try {
      const response = await api.post('/auth/login', credentials);
      
      // If login is successful, store the token
      if (response && response.code === 200 && response.data && response.data.token) {
        localStorage.setItem('auth_token', response.data.token);
        localStorage.setItem('auth_type', response.data.type || 'Bearer');
        localStorage.setItem('user_id', response.data.id);
        localStorage.setItem('user_name', response.data.firstName || response.data.username);
      }
      
      return response;
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  },

  /**
   * Register a new user
   * @param {Object} userData - User registration data
   * @returns {Promise} - Promise that resolves to registration response
   */
  register: async (userData) => {
    try {
      const response = await api.post('/auth/register', userData);
      return response;
    } catch (error) {
      console.error('Registration error:', error);
      throw error;
    }
  },

  /**
   * Check if user is authenticated
   * @returns {Boolean} - True if authenticated, false otherwise
   */
  isAuthenticated: () => {
    return !!localStorage.getItem('auth_token');
  },

  /**
   * Get the authentication token
   * @returns {String|null} - The auth token or null if not authenticated
   */
  getToken: () => {
    return localStorage.getItem('auth_token');
  },

  /**
   * Logout the user
   */
  logout: () => {
    localStorage.removeItem('auth_token');
  }
};

export default authService;