import React, { useState } from 'react';

const FileUploader = ({ onFilesSelected, allowedExtensions = ['.csv', '.xls', '.xlsx'] }) => {
  const [error, setError] = useState('');
  const [isDragging, setIsDragging] = useState(false);

  const validateFiles = (files) => {
    setError('');

    const invalidFiles = files.filter(file => {
      const fileExtension = '.' + file.name.split('.').pop().toLowerCase();
      return !allowedExtensions.includes(fileExtension);
    });

    if (invalidFiles.length > 0) {
      setError(`Invalid file format. Please upload only CSV or Excel files (${allowedExtensions.join(', ')})`);
      return false;
    }

    return true;
  };

  const handleFileChange = (event) => {
    const files = Array.from(event.target.files);

    if (validateFiles(files)) {
      onFilesSelected(files);
    }
  };

  const handleDrop = (event) => {
    event.preventDefault();
    setIsDragging(false);

    const files = Array.from(event.dataTransfer.files);

    if (validateFiles(files)) {
      onFilesSelected(files);
    }
  };

  const handleDragOver = (event) => {
    event.preventDefault();
    setIsDragging(true);
  };

  const handleDragLeave = () => {
    setIsDragging(false);
  };

  return (
    <div className="w-full">
      {/* Dashed border container */}
      <div
        className={`border-2 border-dashed rounded-lg p-16 text-center transition-colors
          ${isDragging ? 'border-indigo-500 bg-indigo-50' : 'border-gray-300'}
        `}
      >
        <div className="flex flex-col items-center justify-center">
          {/* Upload icon */}
          <svg className="mx-auto h-14 w-14 text-gray-400" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
          </svg>

          <p className="mt-4 text-gray-600">Upload files here</p>

          {/* File input - hidden but triggered by the button */}
          <input
            type="file"
            id="fileInput"
            className="hidden"
            accept={allowedExtensions.join(',')}
            onChange={handleFileChange}
            multiple
          />

          {/* Button that triggers the file input */}
          <button
            type="button"
            onClick={() => document.getElementById('fileInput').click()}
            className="mt-8 px-6 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            Select Files
          </button>
        </div>
      </div>

      {/* Error message */}
      {error && (
        <div className="mt-4 text-red-500 text-center">
          {error}
        </div>
      )}
    </div>
  );
};

export default FileUploader;