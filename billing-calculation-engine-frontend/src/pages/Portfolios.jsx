// pages/Portfolios.jsx
export default function Portfolios() {
  // Sample data for portfolios table
  const portfolios = [
    { id: 'PTF001', clientName: 'Acme Corporation', currency: 'CAD', aum: 3500000, assetCount: 12, created: '2024-01-15' },
    { id: 'PTF002', clientName: 'Global Industries', currency: 'USD', aum: 2800000, assetCount: 8, created: '2024-02-22' },
    { id: 'PTF003', clientName: 'Acme Corporation', currency: 'CAD', aum: 5250000, assetCount: 15, created: '2024-03-08' },
    { id: 'PTF004', clientName: 'Northern Trust', currency: 'CAD', aum: 3200000, assetCount: 10, created: '2024-01-30' },
    { id: 'PTF005', clientName: 'Sunrise Capital', currency: 'USD', aum: 4800000, assetCount: 14, created: '2024-04-15' },
    { id: 'PTF006', clientName: 'Atlas Partners', currency: 'CAD', aum: 9500000, assetCount: 22, created: '2024-05-10' },
    { id: 'PTF007', clientName: 'Phoenix Fund', currency: 'USD', aum: 2100000, assetCount: 7, created: '2024-06-01' },
    { id: 'PTF008', clientName: 'Horizon Group', currency: 'CAD', aum: 2800000, assetCount: 9, created: '2024-06-22' },
    { id: 'PTF009', clientName: 'Evergreen Investments', currency: 'USD', aum: 6300000, assetCount: 18, created: '2024-07-12' },
    { id: 'PTF010', clientName: 'Evergreen Investments', currency: 'CAD', aum: 6000000, assetCount: 16, created: '2024-08-05' },
  ];

  // Format currency function
  const formatCurrency = (amount, currency) => {
    return `${currency === 'CAD' ? 'CA$' : 'US$'}${amount.toLocaleString('en-US', { maximumFractionDigits: 2 })}`;
  };

  // Format date function
  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-US', options);
  };

  return (
    <div className="py-6 px-4 sm:px-6 lg:px-8">
      <div className="sm:flex sm:items-center sm:justify-between mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Portfolios</h1>
        
        {/* Search and filter section */}
        <div className="mt-4 sm:mt-0 flex">
          <div className="relative rounded-md shadow-sm">
            <input
              type="text"
              placeholder="Search portfolios..."
              className="focus:ring-indigo-500 focus:border-indigo-500 block w-full pr-10 sm:text-sm border-gray-300 rounded-md"
            />
            <div className="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
              <svg className="h-5 w-5 text-gray-400" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </div>
          </div>
          
          <div className="ml-3">
            <select className="block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md">
              <option>All Currencies</option>
              <option>CAD</option>
              <option>USD</option>
            </select>
          </div>
        </div>
      </div>
      
      {/* Portfolio summary cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        {/* Total Portfolios */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-medium text-gray-500">Total Portfolios</h2>
          <p className="text-3xl font-bold text-gray-900 mt-2">10</p>
        </div>
        
        {/* Total AUM (CAD) */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-medium text-gray-500">Total AUM (CAD)</h2>
          <p className="text-3xl font-bold text-gray-900 mt-2">CA$46,450,000</p>
        </div>
        
        {/* Average Portfolio Size */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-medium text-gray-500">Average Portfolio Size</h2>
          <p className="text-3xl font-bold text-gray-900 mt-2">CA$4,645,000</p>
        </div>
      </div>
      
      {/* Portfolios table */}
      <div className="bg-white shadow overflow-hidden sm:rounded-lg">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Portfolio ID</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Client</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Currency</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">AUM</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Assets</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Created Date</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {portfolios.map((portfolio) => (
                <tr key={portfolio.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm font-medium text-gray-900">{portfolio.id}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-900">{portfolio.clientName}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full 
                      ${portfolio.currency === 'CAD' ? 'bg-green-100 text-green-800' : 'bg-blue-100 text-blue-800'}`}>
                      {portfolio.currency}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {formatCurrency(portfolio.aum, portfolio.currency)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {portfolio.assetCount}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {formatDate(portfolio.created)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    <button className="text-indigo-600 hover:text-indigo-900 mr-3">View</button>
                    <button className="text-gray-600 hover:text-gray-900">Edit</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        
        {/* Pagination */}
        <div className="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
          <div className="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
            <div>
              <p className="text-sm text-gray-700">
                Showing <span className="font-medium">1</span> to <span className="font-medium">10</span> of{' '}
                <span className="font-medium">10</span> results
              </p>
            </div>
            <div>
              <nav className="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
                <a
                  href="#"
                  className="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
                >
                  <span className="sr-only">Previous</span>
                  <svg className="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                    <path fillRule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clipRule="evenodd" />
                  </svg>
                </a>
                <a
                  href="#"
                  aria-current="page"
                  className="z-10 bg-indigo-50 border-indigo-500 text-indigo-600 relative inline-flex items-center px-4 py-2 border text-sm font-medium"
                >
                  1
                </a>
                <a
                  href="#"
                  className="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
                >
                  <span className="sr-only">Next</span>
                  <svg className="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                    <path fillRule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clipRule="evenodd" />
                  </svg>
                </a>
              </nav>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}