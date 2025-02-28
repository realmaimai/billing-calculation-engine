import { useState, useCallback } from 'react';
import { uploadFiles, cancelUpload, validateFiles } from '../services/uploadService';

/**
 * Custom hook to manage file upload state and operations
 * @param {Object} options - Configuration options
 * @returns {Object} - File upload state and methods
 */
const useFileUpload = (options = {}) => {
  const {
    allowedExtensions = ['.csv', '.xls', '.xlsx'],
    maxFileSize = 5 * 1024 * 1024, // 5MB default
    onUploadComplete = null,
    onUploadError = null
  } = options;
  
  const [files, setFiles] = useState([]);
  const [selectedFile, setSelectedFile] = useState(null); // For preview
  const [isUploading, setIsUploading] = useState(false);
  const [progress, setProgress] = useState(0);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);
  const [currentUpload, setCurrentUpload] = useState(null);
  
  // Handle selecting files
  const handleFilesSelected = useCallback((newFiles) => {
    const validation = validateFiles(newFiles, allowedExtensions);
    
    if (!validation.isValid) {
      setError(validation.error);
      return;
    }
    
    setFiles(newFiles);
    setError(null);
    setSuccess(false);
  }, [allowedExtensions]);
  
  // Remove a file from the selection
  const handleRemoveFile = useCallback((index) => {
    setFiles(prevFiles => {
      const updatedFiles = [...prevFiles];
      updatedFiles.splice(index, 1);
      return updatedFiles;
    });
    
    // Reset states if no files left
    if (files.length === 1) {
      setError(null);
      setSuccess(false);
    }
  }, [files]);
  
  // Start the upload process
  const handleStartUpload = useCallback(async () => {
    // Reset states
    setError(null);
    setSuccess(false);
    setProgress(0);
    setIsUploading(true);
    
    try {
      // Create upload promise with progress callback
      const uploadPromise = uploadFiles(files, (progressPercentage) => {
        setProgress(progressPercentage);
      });
      
      // Store reference to current upload for potential cancellation
      setCurrentUpload(uploadPromise);
      
      // Wait for upload to complete
      const response = await uploadPromise;
      
      // Handle success
      setSuccess(true);
      setIsUploading(false);
      setProgress(100);
      
      // Call the success callback if provided
      if (onUploadComplete) {
        onUploadComplete(response);
      }
    } catch (err) {
      // Handle error
      setError(err.message || 'Upload failed');
      setIsUploading(false);
      
      // Call the error callback if provided
      if (onUploadError) {
        onUploadError(err);
      }
    } finally {
      setCurrentUpload(null);
    }
  }, [files, onUploadComplete, onUploadError]);
  
  // Cancel the current upload
  const handleCancelUpload = useCallback(() => {
    if (currentUpload) {
      cancelUpload(currentUpload);
      setIsUploading(false);
      setCurrentUpload(null);
    }
  }, [currentUpload]);
  
  // Retry a failed upload
  const handleRetry = useCallback(() => {
    setError(null);
    handleStartUpload();
  }, [handleStartUpload]);
  
  // Set a file for preview
  const handlePreviewFile = useCallback((file) => {
    setSelectedFile(file);
  }, []);
  
  // Close the preview
  const handleClosePreview = useCallback(() => {
    setSelectedFile(null);
  }, []);
  
  // Reset the upload state
  const resetUpload = useCallback(() => {
    setFiles([]);
    setSelectedFile(null);
    setIsUploading(false);
    setProgress(0);
    setError(null);
    setSuccess(false);
    setCurrentUpload(null);
  }, []);
  
  return {
    // State
    files,
    selectedFile,
    isUploading,
    progress,
    error,
    success,
    
    // Methods
    handleFilesSelected,
    handleRemoveFile,
    handleStartUpload,
    handleCancelUpload,
    handleRetry,
    handlePreviewFile,
    handleClosePreview,
    resetUpload
  };
};

export default useFileUpload;