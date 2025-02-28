// src/pages/Portfolios.jsx
import { useState, useEffect } from 'react';
import portfolioService from '../services/portfolioService';
import AssetModal from '../components/portfolios/AssetModal';

export default function Portfolios() {
  // State for portfolios data
  const [portfolios, setPortfolios] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // State for the selected portfolio (for modal)
  const [selectedPortfolio, setSelectedPortfolio] = useState(null);
  
  // Fetch portfolios on component mount
  useEffect(() => {
    const fetchPortfolios = async () => {
      try {
        setIsLoading(true);
        const response = await portfolioService.getAllPortfolios();
        
        if (response && response.data) {
          setPortfolios(response.data);
        }
      } catch (err) {
        console.error('Error fetching portfolios:', err);
        setError('Failed to load portfolios');
      } finally {
        setIsLoading(false);
      }
    };
    
    fetchPortfolios();
  }, []);
  
  // Format currency
  const formatCurrency = (amount, currency) => {
    const currencySymbol = currency === 'CAD' ? 'CA$' : 'US$';
    return `${currencySymbol}${amount.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  };
  
  // Handle portfolio click to show assets
  const handlePortfolioClick = (portfolio) => {
    setSelectedPortfolio(portfolio);
  };
  
  // Close the asset modal
  const handleCloseModal = () => {
    setSelectedPortfolio(null);
  };
  
  // Loading state
  if (isLoading) {
    return (
      <div className="py-6 px-4 sm:px-6 lg:px-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-6">Portfolios</h1>
        <div className="bg-white shadow rounded-md p-4">
          <p className="text-gray-500">Loading portfolios data...</p>
        </div>
      </div>
    );
  }
  
  // Error state
  if (error) {
    return (
      <div className="py-6 px-4 sm:px-6 lg:px-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-6">Portfolios</h1>
        <div className="bg-red-50 border border-red-200 text-red-800 rounded-md p-4">
          <p className="font-medium">Error loading portfolios</p>
          <p className="mt-1">{error}</p>
          <button 
            className="mt-2 inline-flex items-center px-3 py-1.5 border border-transparent text-xs font-medium rounded text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
            onClick={() => window.location.reload()}
          >
            Reload
          </button>
        </div>
      </div>
    );
  }
  
  // Empty state
  if (portfolios.length === 0) {
    return (
      <div className="py-6 px-4 sm:px-6 lg:px-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-6">Portfolios</h1>
        <div className="bg-white shadow rounded-md p-6 text-center">
          <p className="text-gray-500">No portfolios found.</p>
        </div>
      </div>
    );
  }
  
  // Calculate summary data
  const totalPortfolios = portfolios.length;
  const totalCadAum = portfolios
    .filter(p => p.portfolioCurrency === 'CAD')
    .reduce((sum, p) => sum + p.portfolioAum, 0);
  const totalUsdAum = portfolios
    .filter(p => p.portfolioCurrency === 'USD')
    .reduce((sum, p) => sum + p.portfolioAum, 0);
  const avgPortfolioSize = portfolios.length > 0 
    ? portfolios.reduce((sum, p) => sum + p.portfolioAum, 0) / portfolios.length 
    : 0;
  
  // Render the portfolios table with summary cards
  return (
    <div className="py-6 px-4 sm:px-6 lg:px-8">
      <div className="sm:flex sm:items-center sm:justify-between mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Portfolios</h1>
      </div>
      
      {/* Portfolio summary cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        {/* Total Portfolios */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-medium text-gray-500">Total Portfolios</h2>
          <p className="text-3xl font-bold text-gray-900 mt-2">{totalPortfolios}</p>
        </div>
        
        {/* Total AUM (CAD) */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-medium text-gray-500">Total AUM (CAD)</h2>
          <p className="text-3xl font-bold text-gray-900 mt-2">{formatCurrency(totalCadAum, 'CAD')}</p>
        </div>
        
        {/* Total AUM (USD) */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-medium text-gray-500">Total AUM (USD)</h2>
          <p className="text-3xl font-bold text-gray-900 mt-2">{formatCurrency(totalUsdAum, 'USD')}</p>
        </div>
        
        {/* Average Portfolio Size */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-medium text-gray-500">Average Portfolio Size</h2>
          <p className="text-3xl font-bold text-gray-900 mt-2">{formatCurrency(avgPortfolioSize, 'CAD')}</p>
        </div>
      </div>
      
      {/* Portfolios table */}
      <div className="bg-white shadow overflow-hidden sm:rounded-lg">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Portfolio ID</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Client ID</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Currency</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">AUM</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Fee Amount</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Fee Rate</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {portfolios.map((portfolio) => {
                // Calculate fee rate (avoid division by zero)
                const feeRate = portfolio.portfolioAum > 0 
                  ? (portfolio.portfolioFee / portfolio.portfolioAum) * 100 
                  : 0;
                
                return (
                <tr key={portfolio.portfolioId} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <button 
                      onClick={() => handlePortfolioClick(portfolio)}
                      className="text-sm font-medium text-indigo-600 hover:text-indigo-900 hover:underline focus:outline-none"
                    >
                      {portfolio.portfolioId}
                    </button>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm font-medium text-gray-900">{portfolio.clientId}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full 
                      ${portfolio.portfolioCurrency === 'CAD' ? 'bg-green-100 text-green-800' : 'bg-blue-100 text-blue-800'}`}>
                      {portfolio.portfolioCurrency}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {formatCurrency(portfolio.portfolioAum, portfolio.portfolioCurrency)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {formatCurrency(portfolio.portfolioFee, portfolio.portfolioCurrency)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {feeRate.toFixed(2)}%
                  </td>
                </tr>
              )})}
            </tbody>
          </table>
        </div>
      </div>
      
      {/* Asset Modal - only shown when a portfolio is selected */}
      {selectedPortfolio && (
        <AssetModal 
          portfolio={selectedPortfolio}
          onClose={handleCloseModal}
        />
      )}
    </div>
  );
}