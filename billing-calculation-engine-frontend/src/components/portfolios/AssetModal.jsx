import React, { useState, useEffect } from 'react';
import assetService from '../../services/assetService';

const AssetModal = ({ portfolio, onClose }) => {
  // State for assets data
  const [assets, setAssets] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // Asset types mapping (for display purposes)
  const assetTypes = {
    'US': 'Stock',
    'GOV': 'Bond',
    'CAD': 'Stock'
  };
  
  // Get asset type from asset ID
  const getAssetType = (assetId) => {
    const parts = assetId.split(' ');
    if (parts.length > 1) {
      const typeCode = parts[parts.length - 1];
      return assetTypes[typeCode] || 'Other';
    }
    return 'Other';
  };
  
  // Format currency
  const formatCurrency = (amount, currency = portfolio.portfolioCurrency) => {
    const currencySymbol = currency === 'CAD' ? 'CA$' : 'US$';
    return `${currencySymbol}${amount.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  };
  
  // Fetch assets data when the modal opens
  useEffect(() => {
    const fetchAssets = async () => {
      try {
        setIsLoading(true);
        setError(null);
        
        // Convert portfolio ID to lowercase as per API requirements if needed
        const portfolioId = portfolio.portfolioId.toLowerCase();
        const response = await assetService.getPortfolioAssets(portfolioId);
        
        if (response && response.data) {
          setAssets(response.data);
        } else {
          throw new Error('Invalid response format');
        }
      } catch (err) {
        console.error('Error fetching assets:', err);
        setError('Failed to load portfolio assets');
      } finally {
        setIsLoading(false);
      }
    };
    
    fetchAssets();
  }, [portfolio.portfolioId]);
  
  return (
    <div className="fixed inset-0 bg-gray-600 bg-opacity-75 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-lg shadow-xl max-w-4xl w-full max-h-[90vh] overflow-hidden">
        {/* Modal Header */}
        <div className="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
          <h3 className="text-lg font-medium text-gray-900">
            Portfolio Assets: {portfolio.portfolioId}
          </h3>
          <button 
            onClick={onClose}
            className="text-gray-400 hover:text-gray-500 focus:outline-none"
          >
            <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        {/* Modal Body */}
        <div className="px-6 py-4 max-h-[70vh] overflow-y-auto">
          <div className="mb-4">
            <p className="text-sm text-gray-500">
              Client ID: <span className="font-medium text-gray-900">{portfolio.clientId}</span>
            </p>
            <p className="text-sm text-gray-500">
              Currency: <span className="font-medium text-gray-900">{portfolio.portfolioCurrency}</span>
            </p>
            <p className="text-sm text-gray-500">
              Total AUM: <span className="font-medium text-gray-900">{formatCurrency(portfolio.portfolioAum)}</span>
            </p>
          </div>
          
          {/* Loading state */}
          {isLoading && (
            <div className="py-8 flex justify-center">
              <svg className="animate-spin h-8 w-8 text-indigo-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
            </div>
          )}
          
          {/* Error state */}
          {error && !isLoading && (
            <div className="py-4 bg-red-50 border border-red-200 text-red-800 rounded-md p-4">
              <p className="font-medium">Error</p>
              <p className="mt-1">{error}</p>
            </div>
          )}
          
          {/* Data display */}
          {!isLoading && !error && (
            <>
              {assets.length > 0 ? (
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Asset ID</th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Type</th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Value</th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Currency</th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {assets.map((asset, index) => {
                      const assetType = getAssetType(asset.assetId);
                      
                      return (
                        <tr key={`${asset.assetId}-${index}`} className="hover:bg-gray-50">
                          <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{asset.assetId}</td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                            <span className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full 
                              ${assetType === 'Stock' ? 'bg-blue-100 text-blue-800' : 
                                assetType === 'Bond' ? 'bg-purple-100 text-purple-800' : 
                                assetType === 'Cash' ? 'bg-green-100 text-green-800' : 
                                'bg-gray-100 text-gray-800'}`}>
                              {assetType}
                            </span>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{asset.date}</td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{formatCurrency(asset.assetValue, asset.currency)}</td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{asset.currency}</td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              ) : (
                <div className="py-8 text-center text-gray-500">
                  No assets found for this portfolio.
                </div>
              )}
            </>
          )}
        </div>
        
        {/* Modal Footer */}
        <div className="px-6 py-4 bg-gray-50 border-t border-gray-200 flex justify-end">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-indigo-600 text-white text-sm font-medium rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

export default AssetModal;