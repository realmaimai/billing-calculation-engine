// src/services/api.js

// Base API URL
const API_BASE_URL = 'http://localhost:8080/api/v1';

// Set your JWT token here
const AUTH_TOKEN = 'eyJhbGciOiJIUzM4NCJ9.eyJ1c2VySWQiOjEsImlhdCI6MTc0MDY1ODU3NSwiZXhwIjoxNzQwNzQ0OTc1fQ.AcAgzr7KsvRSv72BiKuFFaRy7s3ig2a9vHlJff6wK4vhwBHgfzv7X2qhtlUZCWlU'; 

/**
 * Simple fetch wrapper for API calls
 * @param {string} endpoint - API endpoint to call
 * @param {Object} options - Fetch options
 * @returns {Promise} - Promise that resolves to the API response
 */
export const fetchApi = async (endpoint, options = {}) => {
  const url = `${API_BASE_URL}${endpoint}`;
  
  const defaultHeaders = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${AUTH_TOKEN}`
  };
  
  const fetchOptions = {
    ...options,
    headers: {
      ...defaultHeaders,
      ...options.headers,
    },
  };
  
  try {
    const response = await fetch(url, fetchOptions);
    
    if (!response.ok) {
      throw new Error(`API error: ${response.status}`);
    }
    
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      return await response.json();
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
      
      // Add authorization header
      xhr.setRequestHeader('Authorization', `Bearer ${AUTH_TOKEN}`);
      
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