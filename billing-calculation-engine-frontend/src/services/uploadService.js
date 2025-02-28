const API_URL = 'http://localhost:8080/api/v1';

const AUTH_TOKEN = 'eyOjEsImlhdCI6MTc0MDY1ODU3NSwiZXhwIjoxNzQwNzQ0OTc1fQ.AcAgzr7KsvRSv72BiKuFFaRy7s3ig2a9vHlJff6wK4vhwBHgfzv7X2qhtlUZCWlU'; 

/**
 * Upload a single file to the server with progress tracking
 * @param {File} file - The file to upload
 * @param {Function} onProgress - Callback for upload progress updates
 * @returns {Promise} - Promise that resolves to the server response
 */
export const uploadFile = async (file, onProgress) => {
  // Create FormData to send file
  const formData = new FormData();
  
  // Append the file with the name 'file' (or whatever your backend expects)
  formData.append('file', file);
  
  try {
    // XMLHttpRequest to track upload progress
    const xhr = new XMLHttpRequest();
    
    const promise = new Promise((resolve, reject) => {
      xhr.open('POST', `${API_URL}/files/upload`, true);
      
      // set authorization header
      xhr.setRequestHeader('Authorization', `Bearer ${AUTH_TOKEN}`);
      
      xhr.onload = function() {
        if (xhr.status >= 200 && xhr.status < 300) {
          try {
            const response = JSON.parse(xhr.responseText);
            console.log('Upload response from backend:', response);

            resolve(response);
          } catch (e) {
            resolve(xhr.responseText);
          }
        } else {
          reject(new Error(`Upload failed with status: ${xhr.status}`));
        }
      };
      
      xhr.onerror = function() {
        reject(new Error('Network error occurred during upload'));
      };
      
      // Track upload progress
      xhr.upload.onprogress = function(event) {
        if (event.lengthComputable && onProgress) {
          const percentComplete = Math.round((event.loaded / event.total) * 100);
          onProgress(percentComplete);
        }
      };
    });
    
    // Store xhr on the promise for access to abort
    promise.xhr = xhr;
    
    // Send the form data
    xhr.send(formData);
    
    return promise;
  } catch (error) {
    throw new Error(`Upload service error: ${error.message}`);
  }
};

/**
 * Abort an in-progress upload
 * @param {Promise} uploadPromise - The promise returned from uploadFile
 */
export const cancelUpload = (uploadPromise) => {
  if (uploadPromise && uploadPromise.xhr) {
    uploadPromise.xhr.abort();
  }
};

/**
 * Validate a file before uploading
 * @param {File} file - The file to validate
 * @param {String[]} allowedExtensions - Array of allowed file extensions
 * @returns {Object} - Validation result with isValid and error
 */
export const validateFile = (file, allowedExtensions = ['.csv', '.xls', '.xlsx']) => {
  // Check if file exists
  if (!file) {
    return {
      isValid: false,
      error: 'No file selected'
    };
  }
  
  // Check file extension
  const fileExtension = '.' + file.name.split('.').pop().toLowerCase();
  if (!allowedExtensions.includes(fileExtension)) {
    return {
      isValid: false,
      error: `Invalid file format. Please upload only allowed file types (${allowedExtensions.join(', ')})`
    };
  }
  
  // Check file size (adjust the maximum size as needed)
  const MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
  if (file.size > MAX_FILE_SIZE) {
    return {
      isValid: false,
      error: `File exceeds the maximum allowed size of 5MB`
    };
  }
  
  return {
    isValid: true,
    error: null
  };
};

export default {
  uploadFile,
  cancelUpload,
  validateFile
};