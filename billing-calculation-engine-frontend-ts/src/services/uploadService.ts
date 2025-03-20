import apiClient from './apiClient';

const uploadService = {
  // Upload file
  uploadFile: async (file: File, onProgress?: (percentage: number) => void) => {
    const formData = new FormData();
    formData.append('file', file);

    const response = await apiClient.post('/files/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      onUploadProgress: onProgress ? (e) => {
        const percentage = Math.round((e.loaded * 100) / (e.total || 1));
        onProgress(percentage);
      } : undefined
    });

    return response.data;
  },

  // Validate file
  validateFile: (file: File | null, allowedTypes = ['.csv', '.xls', '.xlsx']) => {
    if (!file) {
      return { isValid: false, error: 'No file selected' };
    }

    const extension = '.' + file.name.split('.').pop()?.toLowerCase();
    if (!allowedTypes.includes(extension)) {
      return {
        isValid: false,
        error: `Invalid file format. Allowed: ${allowedTypes.join(', ')}`
      };
    }

    const MAX_SIZE = 10 * 1024 * 1024; // 10MB
    if (file.size > MAX_SIZE) {
      return { isValid: false, error: 'File exceeds 10MB limit' };
    }

    return { isValid: true, error: null };
  }
};

export default uploadService;