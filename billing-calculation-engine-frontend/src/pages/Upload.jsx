import { useState } from 'react';
import FileUploader from '../components/upload/FileUploader';
import UploadStatus from '../components/upload/UploadStatus';
import UploadResult from '../components/upload/UploadResult';
import { uploadFile, validateFile } from '../services/uploadService';

export default function Upload() {
  const [selectedFile, setSelectedFile] = useState(null);
  const [isUploading, setIsUploading] = useState(false);
  const [progress, setProgress] = useState(0);
  const [error, setError] = useState(null);
  const [uploadResult, setUploadResult] = useState(null);

  
  const handleFileSelected = (files) => {
    if (files && files.length > 0) {
      const file = files[0]; 
      const validation = validateFile(file);
      
      if (!validation.isValid) {
        setError(validation.error);
        return;
      }
      
      setSelectedFile(file);
      setError(null);
      setUploadResult(null);
    }
  };
  
  const handleRemoveFile = () => {
    setSelectedFile(null);
    setError(null);
    setUploadResult(null);
  };
  
  const handleStartUpload = async () => {
    if (!selectedFile) return;
    
    // Reset states
    setError(null);
    setProgress(0);
    setIsUploading(true);
    setUploadResult(null);
    
    try {
      // Upload file with progress tracking
      const response = await uploadFile(selectedFile, (progressPercentage) => {
        setProgress(progressPercentage);
      });
      
      // Set the result from the backend
      setUploadResult(response);
      
      // Check if response has error code
      if (response.code >= 400) {
        // Handle as error but still display the result
        console.log('Upload completed with error:', response);
      } else {
        console.log('Upload completed successfully:', response);
      }
    } catch (err) {
      setError(err.message || 'Upload failed');
    } finally {
      setIsUploading(false);
    }
  };
  
  const handleRetry = () => {
    setError(null);
    handleStartUpload();
  };
  
  const handleReset = () => {
    setSelectedFile(null);
    setIsUploading(false);
    setProgress(0);
    setError(null);
    setUploadResult(null);
  };
  
  return (
    <div className="py-6 w-full max-w-full">
      <h1 className="text-4xl font-bold text-center mb-10">Upload Files</h1>
      
      <div className="bg-white shadow rounded-lg p-8 max-w-2xl mx-auto">
        <p className="text-gray-600 mb-6">
          Upload your CSV or Excel files here to process billing calculations.
        </p>
        
        {/* Show file uploader if no file is selected or after a reset */}
        {!selectedFile && (
          <FileUploader onFilesSelected={handleFileSelected} />
        )}
        
        {/* Show upload status when file is selected */}
        {selectedFile && (
          <div className="mb-6">
            <div className="flex items-center justify-between border p-3 rounded">
              <div className="flex items-center">
                <svg className="h-5 w-5 text-gray-400 mr-2" fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M4 4a2 2 0 012-2h4.586A2 2 0 0112 2.586L15.414 6A2 2 0 0116 7.414V16a2 2 0 01-2 2H6a2 2 0 01-2-2V4z" clipRule="evenodd" />
                </svg>
                <span className="text-gray-700">{selectedFile.name}</span>
                <span className="ml-2 text-gray-500">({(selectedFile.size / 1024).toFixed(2)} KB)</span>
              </div>
              
              {!isUploading && !uploadResult && (
                <button 
                  onClick={handleRemoveFile}
                  className="text-gray-400 hover:text-gray-600"
                >
                  <svg className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                    <path fillRule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clipRule="evenodd" />
                  </svg>
                </button>
              )}
            </div>
            
            {/* Progress bar */}
            {isUploading && (
              <div className="mt-4">
                <div className="flex justify-between mb-1">
                  <span className="text-sm font-medium text-gray-700">Uploading...</span>
                  <span className="text-sm font-medium text-gray-700">{progress}%</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2.5">
                  <div 
                    className="bg-indigo-600 h-2.5 rounded-full transition-all duration-300" 
                    style={{ width: `${progress}%` }}
                  ></div>
                </div>
              </div>
            )}
            
            {/* Error message */}
            {error && (
              <div className="mt-4 bg-red-50 border border-red-200 text-red-800 rounded-md p-4">
                <div className="flex">
                  <svg className="h-5 w-5 text-red-400 mr-2" viewBox="0 0 20 20" fill="currentColor">
                    <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
                  </svg>
                  <div>
                    <p className="text-sm font-medium">Error uploading file</p>
                    <p className="text-sm mt-1">{error}</p>
                    <button
                      onClick={handleRetry}
                      className="mt-2 inline-flex items-center px-3 py-1.5 border border-transparent text-xs font-medium rounded text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
                    >
                      Retry
                    </button>
                  </div>
                </div>
              </div>
            )}
            
            {/* Action buttons */}
            {!isUploading && !uploadResult && (
              <div className="mt-4 flex justify-end">
                <button
                  onClick={handleStartUpload}
                  type="button"
                  className="px-4 py-2 bg-indigo-600 text-white rounded-md text-sm font-medium hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                >
                  Upload File
                </button>
              </div>
            )}
          </div>
        )}
        
        {/* Upload Result Display */}
        {uploadResult && (
          <>
            <UploadResult result={uploadResult} />
            
            <div className="mt-6 flex justify-end">
              <button
                onClick={handleReset}
                className="px-4 py-2 bg-indigo-600 text-white rounded-md text-sm font-medium hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                Upload Another File
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}