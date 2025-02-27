import React, { useEffect } from 'react';

const UploadResult = ({ result }) => {
  useEffect(() => {
    // Debug log to see what's being passed to the component
    console.log('UploadResult received:', result);
  }, [result]);

  if (!result) {
    console.log('UploadResult: No result data provided');
    return null;
  }

  // Handle different response structures - extract data from response
  const data = result.data || result;
  const status = data.status || 'FAILED';
  const isSuccess = status === 'SUCCESS';
  const processingResult = data.processingResult || '';
  const processingLines = processingResult ? processingResult.split('\n') : [];
  
  // Display error message if provided
  const errorMessage = result.msg;
  
  console.log('UploadResult processed data:', {
    data,
    status,
    isSuccess,
    processingResult: processingResult.substring(0, 100) + '...',
    errorMessage
  });

  return (
    <div className="mt-6 border rounded-lg overflow-hidden">
      <div className={`px-4 py-3 text-white font-medium ${isSuccess ? 'bg-green-600' : 'bg-red-600'}`}>
        Upload Status: {status} {result.code && `(Code: ${result.code})`}
      </div>
      
      <div className="p-4 bg-white">
        {errorMessage && (
          <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded text-red-600">
            {errorMessage}
          </div>
        )}
      
        <div className="mb-2">
          <span className="font-medium">File: </span>
          <span>{data.fileName}</span>
        </div>
        
        <div className="mb-2">
          <span className="font-medium">Size: </span>
          <span>{(data.fileSize / 1024).toFixed(2)} KB</span>
        </div>
        
        {processingResult && (
          <div className="mt-3">
            <h4 className="font-medium mb-2">Processing Result:</h4>
            <div className="bg-gray-50 p-3 rounded border max-h-64 overflow-y-auto text-sm font-mono">
              {processingLines.map((line, index) => (
                <div key={index} className={line.includes('Error:') ? 'text-red-600' : ''}>
                  {line}
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default UploadResult;