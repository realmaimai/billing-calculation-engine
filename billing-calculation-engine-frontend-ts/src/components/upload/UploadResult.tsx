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
  const status = result.code < 400 ? 'SUCCESS' : 'FAILED';
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
        {errorMessage && !isSuccess && (
          <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded text-red-600">
            {errorMessage}
          </div>
        )}

        <div className="mb-2">
          <span className="font-medium">File: </span>
          <span>{data.fileName || data.name || 'Unknown'}</span>
        </div>

        <div className="mb-2">
          <span className="font-medium">Size: </span>
          <span>{((data.fileSize || data.size || 0) / 1024).toFixed(2)} KB</span>
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

        {isSuccess && (
          <div className="mt-4 p-3 bg-green-50 border border-green-200 rounded text-green-600">
            <div className="flex items-center">
              <svg className="h-5 w-5 text-green-400 mr-2" viewBox="0 0 20 20" fill="currentColor">
                <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
              </svg>
              <div>
                <p className="text-sm font-medium">File processed successfully!</p>
                {data.message && <p className="text-sm mt-1">{data.message}</p>}
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default UploadResult;