import React, { useState, useEffect } from 'react';

const FilePreview = ({ file, onClose }) => {
  const [preview, setPreview] = useState({
    headers: [],
    rows: [],
    isLoading: true,
    error: null
  });

  // Determine file type for appropriate preview
  const fileType = file?.name.split('.').pop().toLowerCase();

  useEffect(() => {
    if (!file) {
      setPreview({
        headers: [],
        rows: [],
        isLoading: false,
        error: 'No file selected'
      });
      return;
    }

    const readFile = async () => {
      try {
        setPreview(prev => ({ ...prev, isLoading: true, error: null }));

        // For CSV files
        if (fileType === 'csv') {
          const text = await readTextFile(file);
          const { headers, rows } = parseCSV(text);

          setPreview({
            headers,
            rows: rows.slice(0, 10), // Limit to first 10 rows for preview
            totalRows: rows.length,
            isLoading: false,
            error: null
          });
        }
        // For Excel files, we'd need a library like xlsx 
        // This is a placeholder for the actual implementation
        else if (['xls', 'xlsx'].includes(fileType)) {
          setPreview({
            headers: [],
            rows: [],
            isLoading: false,
            error: 'Excel file preview is not implemented yet. File will be processed on the server.'
          });
        }
      } catch (error) {
        setPreview({
          headers: [],
          rows: [],
          isLoading: false,
          error: `Error reading file: ${error.message}`
        });
      }
    };

    readFile();
  }, [file, fileType]);

  // Read file as text
  const readTextFile = (file) => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = (e) => resolve(e.target.result);
      reader.onerror = (e) => reject(new Error('Error reading file'));
      reader.readAsText(file);
    });
  };

  // Simple CSV parser (would need more robust implementation for production)
  const parseCSV = (text) => {
    const lines = text.split('\n').filter(line => line.trim());
    const headers = lines[0].split(',').map(header => header.trim());

    const rows = lines.slice(1).map(line => {
      const values = line.split(',').map(value => value.trim());
      return headers.reduce((obj, header, i) => {
        obj[header] = values[i] || '';
        return obj;
      }, {});
    });

    return { headers, rows };
  };

  return (
    <div className="bg-white rounded-lg shadow-lg overflow-hidden max-w-full">
      <div className="flex justify-between items-center bg-gray-50 px-4 py-3 border-b">
        <h3 className="text-lg font-medium text-gray-900">
          File Preview: {file?.name}
        </h3>
        <button
          onClick={onClose}
          className="text-gray-400 hover:text-gray-500"
        >
          <svg className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
            <path fillRule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clipRule="evenodd" />
          </svg>
        </button>
      </div>

      <div className="px-4 py-4">
        {/* File metadata */}
        <div className="mb-4 grid grid-cols-2 gap-4 text-sm">
          <div>
            <span className="font-medium text-gray-500">File Type:</span>{' '}
            <span className="text-gray-900 uppercase">{fileType}</span>
          </div>
          <div>
            <span className="font-medium text-gray-500">Size:</span>{' '}
            <span className="text-gray-900">{(file?.size / 1024).toFixed(2)} KB</span>
          </div>
          {preview.totalRows && (
            <div>
              <span className="font-medium text-gray-500">Total Rows:</span>{' '}
              <span className="text-gray-900">{preview.totalRows}</span>
            </div>
          )}
        </div>

        {/* Loading state */}
        {preview.isLoading && (
          <div className="py-8 text-center">
            <svg className="animate-spin h-8 w-8 mx-auto text-indigo-500" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
              <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
              <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            <p className="mt-2 text-gray-600">Loading file preview...</p>
          </div>
        )}

        {/* Error state */}
        {preview.error && (
          <div className="py-4 text-center text-red-600">
            {preview.error}
          </div>
        )}

        {/* Data preview */}
        {!preview.isLoading && !preview.error && preview.headers.length > 0 && (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  {preview.headers.map((header, index) => (
                    <th
                      key={index}
                      scope="col"
                      className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                      {header}
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {preview.rows.map((row, rowIndex) => (
                  <tr key={rowIndex} className={rowIndex % 2 === 0 ? 'bg-white' : 'bg-gray-50'}>
                    {preview.headers.map((header, colIndex) => (
                      <td
                        key={`${rowIndex}-${colIndex}`}
                        className="px-6 py-2 whitespace-nowrap text-sm text-gray-500"
                      >
                        {row[header]}
                      </td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>

            {preview.totalRows > preview.rows.length && (
              <div className="py-3 text-center text-sm text-gray-500">
                Showing {preview.rows.length} of {preview.totalRows} rows
              </div>
            )}
          </div>
        )}
      </div>

      <div className="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
        <button
          type="button"
          onClick={onClose}
          className="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
        >
          Close
        </button>
      </div>
    </div>
  );
};

export default FilePreview;