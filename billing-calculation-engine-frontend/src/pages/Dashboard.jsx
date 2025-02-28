// src/pages/Dashboard.jsx
import { useState, useEffect } from 'react';
import dashboardService from '../services/dashboardService';

export default function Dashboard() {
  // State for dashboard data
  const [summary, setSummary] = useState({
    totalAum: 0,
    totalFee: 0,
    totalClient: 0,
    updateDate: ''
  });
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // Fetch dashboard summary on component mount
  useEffect(() => {
    const fetchSummary = async () => {
      try {
        setIsLoading(true);
        const response = await dashboardService.getSummary();
        
        if (response && response.data) {
          setSummary(response.data);
        }
      } catch (err) {
        console.error('Error fetching dashboard summary:', err);
        setError('Failed to load dashboard data');
      } finally {
        setIsLoading(false);
      }
    };
    
    fetchSummary();
  }, []);
  
  // Format currency
  const formatCurrency = (amount) => {
    return `$${amount.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  };
  
  // Format date
  const formatDate = (dateString) => {
    if (!dateString) return '';
    
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-US', options);
  };
  
  // Calculate effective fee rate (avoid division by zero)
  const effectiveFeeRate = summary.totalAum > 0 
    ? (summary.totalFee / summary.totalAum) * 100 
    : 0;
  
  // Loading state
  if (isLoading) {
    return (
      <div className="py-6 px-4 sm:px-6 lg:px-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-6">Dashboard</h1>
        <div className="bg-white shadow rounded-md p-4">
          <p className="text-gray-500">Loading dashboard data...</p>
        </div>
      </div>
    );
  }
  
  // Error state
  if (error) {
    return (
      <div className="py-6 px-4 sm:px-6 lg:px-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-6">Dashboard</h1>
        <div className="bg-red-50 border border-red-200 text-red-800 rounded-md p-4">
          <p className="font-medium">Error loading dashboard</p>
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
  
  return (
    <div className="py-6 px-4 sm:px-6 lg:px-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
        <div className="text-sm text-gray-500">
          Last updated: {formatDate(summary.updateDate)}
        </div>
      </div>
      
      {/* Key Metrics Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
        {/* Total Revenue Card */}
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <div className="px-6 py-5 border-b border-gray-200">
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-medium text-gray-500">Total Revenue</h2>
              <div className="p-2 rounded-full bg-green-100">
                <svg className="h-6 w-6 text-green-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </div>
            </div>
          </div>
          <div className="px-6 py-5">
            <p className="text-3xl font-bold text-gray-900">{formatCurrency(summary.totalFee)}</p>
          </div>
        </div>
        
        {/* Total AUM Card */}
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <div className="px-6 py-5 border-b border-gray-200">
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-medium text-gray-500">Total AUM</h2>
              <div className="p-2 rounded-full bg-indigo-100">
                <svg className="h-6 w-6 text-indigo-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                </svg>
              </div>
            </div>
          </div>
          <div className="px-6 py-5">
            <p className="text-3xl font-bold text-gray-900">{formatCurrency(summary.totalAum)}</p>
          </div>
        </div>
        
        {/* Total Clients Card */}
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <div className="px-6 py-5 border-b border-gray-200">
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-medium text-gray-500">Total Clients</h2>
              <div className="p-2 rounded-full bg-blue-100">
                <svg className="h-6 w-6 text-blue-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
                </svg>
              </div>
            </div>
          </div>
          <div className="px-6 py-5">
            <p className="text-3xl font-bold text-gray-900">{summary.totalClient}</p>
          </div>
        </div>
        
      </div>
      
      {/* Welcome message */}
      <div className="bg-white shadow overflow-hidden sm:rounded-lg mb-8">
        <div className="px-6 py-5">
          <h2 className="text-lg font-medium text-gray-900">Welcome to Billing Calculation Engine</h2>
          <p className="mt-2 text-gray-600">
            Use the navigation to explore clients, portfolios, and billing data.
            This dashboard shows the summary of your billing data as of {formatDate(summary.updateDate)}.
          </p>
        </div>
      </div>
      
      {/* Next steps or quick links */}
      <div className="bg-white shadow overflow-hidden sm:rounded-lg">
        <div className="px-6 py-5 border-b border-gray-200">
          <h2 className="text-lg font-medium text-gray-900">Quick Actions</h2>
        </div>
        <div className="px-6 py-5">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <a 
              href="/clients" 
              className="px-4 py-3 bg-indigo-50 rounded-lg text-indigo-700 flex items-center hover:bg-indigo-100 transition-colors"
            >
              <svg className="h-5 w-5 mr-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
              </svg>
              View Clients
            </a>
            <a 
              href="/portfolios" 
              className="px-4 py-3 bg-indigo-50 rounded-lg text-indigo-700 flex items-center hover:bg-indigo-100 transition-colors"
            >
              <svg className="h-5 w-5 mr-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
              </svg>
              View Portfolios
            </a>
            <a 
              href="/upload" 
              className="px-4 py-3 bg-indigo-50 rounded-lg text-indigo-700 flex items-center hover:bg-indigo-100 transition-colors"
            >
              <svg className="h-5 w-5 mr-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12" />
              </svg>
              Upload Data
            </a>
          </div>
        </div>
      </div>
    </div>
  );
}