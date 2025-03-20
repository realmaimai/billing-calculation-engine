import { useState, useCallback } from 'react';
import uploadService from '../services/uploadService';

interface UploadResponse {
  success: boolean;
  fileId?: string;
  message?: string;
  [key: string]: any;
}

interface FileUploadOptions {
  allowedExtensions?: string[];
  onUploadComplete?: (response: UploadResponse) => void;
  onUploadError?: (error: Error) => void;
}

interface UploadPromise extends Promise<UploadResponse> {
  cancel?: () => void;
}

const useFileUpload = (options: FileUploadOptions = {}) => {
  const {
    allowedExtensions = ['.csv', '.xls', '.xlsx'],
    onUploadComplete,
    onUploadError
  } = options;

  const [file, setFile] = useState<File | null>(null);
  const [isUploading, setIsUploading] = useState<boolean>(false);
  const [progress, setProgress] = useState<number>(0);
  const [error, setError] = useState<string | null>(null);

  // Handle file selection
  const handleFileSelected = useCallback((newFile: File | null) => {
    if (!newFile) {
      setError('No file selected');
      return;
    }

    const result = uploadService.validateFile(newFile, allowedExtensions);

    if (!result.isValid) {
      setError(result.error);
      return;
    }

    setFile(newFile);
    setError(null);
  }, [allowedExtensions]);

  // Start upload process
  const handleStartUpload = useCallback(async () => {
    if (!file) {
      setError('No file to upload');
      return;
    }

    // Reset states
    setError(null);
    setProgress(0);
    setIsUploading(true);

    try {
      // Upload the file with progress tracking
      const uploadPromise = uploadService.uploadFile(file, (progressPercentage) => {
        setProgress(progressPercentage);
      }) as UploadPromise;

      // Wait for upload to complete
      const response = await uploadPromise;

      // Handle success
      setIsUploading(false);
      setProgress(100);

      if (onUploadComplete) {
        onUploadComplete(response);
      }
    } catch (err) {
      // Handle error
      const error = err as Error;
      setError(error.message || 'Upload failed');
      setIsUploading(false);

      if (onUploadError) {
        onUploadError(error);
      }
    }
  }, [file, onUploadComplete, onUploadError]);


  // Retry a failed upload
  const handleRetry = useCallback(() => {
    setError(null);
    handleStartUpload();
  }, [handleStartUpload]);

  // Reset the upload state
  const resetUpload = useCallback(() => {
    setFile(null);
    setIsUploading(false);
    setProgress(0);
    setError(null);
  }, []);

  return {
    file,
    isUploading,
    progress,
    error,

    handleFileSelected,
    handleStartUpload,
    handleRetry,
    resetUpload
  };
};

export default useFileUpload;