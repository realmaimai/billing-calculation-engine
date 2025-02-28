import React from 'react';

const UploadStatus = ({
  files = [],
  isUploading = false,
  progress = 0,
  error = null,
  success = false,
  onRemoveFile,
  onStartUpload,
  onRetry,
  onCancel
}) => {
  return (
    <div className="mt-6 w-full">
      {/* Selected files display */}
      {files.length > 0 && (
        <>
          <h3 className="text-sm font-medium text-gray-700 mb-2">Selected Files:</h3>
          <ul className="border rounded-md divide-y">
            {files.map((file, index) => (
              <li key={index} className="px-4 py-2 flex items-center justify-between text-sm">
                <div className="flex items-center">
                  <svg className="h-5 w-5 text-gray-400 mr-2" fill="currentColor" viewBox="0 0 20 20">
                    <path fillRule="evenodd" d="M4 4a2 2 0 012-2h4.586A2 2 0 0112 2.586L15.414 6A2 2 0 0116 7.414V16a2 2 0 01-2 2H6a2 2 0 01-2-2V4z" clipRule="evenodd" />
                  </svg>
                  <span className="text-gray-700">{file.name}</span>
                  <span className="ml-2 text-gray-500">({(file.size / 1024).toFixed(2)} KB)</span>
                </div>
                {!isUploading && (
                  <button
                    onClick={() => onRemoveFile && onRemoveFile(index)}
                    className="text-gray-400 hover:text-gray-600"
                  >
                    <svg className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                      <path fillRule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clipRule="evenodd" />
                    </svg>
                  </button>
                )}
              </li>
            ))}
          </ul>

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
                  <p className="text-sm font-medium">Error uploading files</p>
                  <p className="text-sm mt-1">{error}</p>
                  <button
                    onClick={onRetry}
                    className="mt-2 inline-flex items-center px-3 py-1.5 border border-transparent text-xs font-medium rounded text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
                  >
                    Retry
                  </button>
                </div>
              </div>
            </div>
          )}

          {/* Success message */}
          {success && !error && (
            <div className="mt-4 bg-green-50 border border-green-200 text-green-800 rounded-md p-4">
              <div className="flex">
                <svg className="h-5 w-5 text-green-400 mr-2" viewBox="0 0 20 20" fill="currentColor">
                  <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                </svg>
                <p className="text-sm font-medium">Files uploaded successfully!</p>
              </div>
            </div>
          )}

          {/* Action buttons */}
          <div className="mt-4 flex justify-end space-x-3">
            {!isUploading && !success && (
              <button
                onClick={onStartUpload}
                type="button"
                className="px-4 py-2 bg-indigo-600 text-white rounded-md text-sm font-medium hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                Upload Files
              </button>
            )}

            {isUploading && (
              <button
                onClick={onCancel}
                type="button"
                className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md text-sm font-medium hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                Cancel
              </button>
            )}
          </div>
        </>
      )}
    </div>
  );
};

export default UploadStatus;