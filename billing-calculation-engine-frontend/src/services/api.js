// src/services/api.js

// Base API URL
const API_BASE_URL = 'http://localhost:8080/api/v1';

/**
 * Simple fetch wrapper for API calls
 * @param {string} endpoint - API endpoint to call
 * @param {Object} options - Fetch options
 * @returns {Promise} - Promise that resolves to the API response
 */
export const fetchApi = async (endpoint, options = {}) => {
  const url = `${API_BASE_URL}${endpoint}`;
  
  // Get auth token from local storage
  const authToken = localStorage.getItem('auth_token');
  const authType = localStorage.getItem('auth_type') || 'Bearer';
  
  const defaultHeaders = {
    'Content-Type': 'application/json'
  };
  
  // Add auth header if token exists
  if (authToken) {
    defaultHeaders['Authorization'] = `${authType} ${authToken}`;
  }
  
  const fetchOptions = {
    ...options,
    headers: {
      ...defaultHeaders,
      ...options.headers,
    },
  };
  
  try {
    const response = await fetch(url, fetchOptions);
    
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      const jsonResponse = await response.json();
      return jsonResponse;
    }
    
    return await response.text();
  } catch (error) {
    console.error('API request failed:', error);
    throw error;
  }
};

// Common API methods
export default {
  // GET request
  get: (endpoint, params = {}) => {
    const queryString = new URLSearchParams(params).toString();
    const url = queryString ? `${endpoint}?${queryString}` : endpoint;
    
    return fetchApi(url, {
      method: 'GET',
    });
  },
  
  // POST request
  post: (endpoint, data = {}) => {
    return fetchApi(endpoint, {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },
  
  // PUT request
  put: (endpoint, data = {}) => {
    return fetchApi(endpoint, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  },
  
  // DELETE request
  delete: (endpoint) => {
    return fetchApi(endpoint, {
      method: 'DELETE',
    });
  },
  
  // File upload
  upload: (endpoint, formData) => {
    return new Promise((resolve, reject) => {
      const xhr = new XMLHttpRequest();
      const url = `${API_BASE_URL}${endpoint}`;
      
      xhr.open('POST', url, true);
      
      // Get auth token from local storage
      const authToken = localStorage.getItem('auth_token');
      const authType = localStorage.getItem('auth_type') || 'Bearer';
      
      // Add authorization header if token exists
      if (authToken) {
        xhr.setRequestHeader('Authorization', `${authType} ${authToken}`);
      }
      
      xhr.onload = function() {
        if (xhr.status >= 200 && xhr.status < 300) {
          try {
            const response = JSON.parse(xhr.responseText);
            resolve(response);
          } catch {
            resolve(xhr.responseText);
          }
        } else {
          reject(new Error(`Upload failed with status: ${xhr.status}`));
        }
      };
      
      xhr.onerror = function() {
        reject(new Error('Network error during upload'));
      };
      
      xhr.send(formData);
    });
  }
};