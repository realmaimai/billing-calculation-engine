// pages/Dashboard.jsx
import { Card, CardHeader, CardBody } from '../components/ui/Card';
import { Badge } from '../components/ui/Badges';
import { Button } from '../components/ui/Button';
import { Alert } from '../components/ui/Alert';

export default function Dashboard() {
  return (
    <div className="py-6 px-4 sm:px-6 lg:px-8 w-full">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
        
        <div className="flex space-x-3">
          <Button size="sm" variant="secondary">Export Data</Button>
          <Button size="sm">Refresh</Button>
        </div>
      </div>
      
      {/* Alert component example */}
      <Alert 
        type="info" 
        title="Welcome to the Billing Calculation Engine" 
        onDismiss={() => console.log('dismissed')}
      >
        This dashboard shows the current billing information as of December 31, 2024.
      </Alert>
      
      <div className="my-6"></div>
      
      {/* Key Metrics Section using Card components */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        {/* Total Revenue Card */}
        <Card>
          <CardBody>
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-medium text-gray-500">Total Revenue</h2>
              <div className="p-2 rounded-full bg-green-100">
                <svg className="h-6 w-6 text-green-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </div>
            </div>
            <div className="mt-4">
              <p className="text-3xl font-bold text-gray-900">$1,245,890</p>
              <p className="text-sm text-gray-500 mt-1">As of December 31, 2024</p>
            </div>
          </CardBody>
        </Card>
        
        {/* Total Clients Card */}
        <Card>
          <CardBody>
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-medium text-gray-500">Total Clients</h2>
              <div className="p-2 rounded-full bg-blue-100">
                <svg className="h-6 w-6 text-blue-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
                </svg>
              </div>
            </div>
            <div className="mt-4">
              <p className="text-3xl font-bold text-gray-900">128</p>
              <p className="text-sm text-gray-500 mt-1">5% increase from last quarter</p>
            </div>
          </CardBody>
        </Card>
        
        {/* Average Fee Rate Card */}
        <Card>
          <CardBody>
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-medium text-gray-500">Average Fee Rate</h2>
              <div className="p-2 rounded-full bg-purple-100">
                <svg className="h-6 w-6 text-purple-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                </svg>
              </div>
            </div>
            <div className="mt-4">
              <p className="text-3xl font-bold text-gray-900">0.85%</p>
              <p className="text-sm text-gray-500 mt-1">Based on all clients</p>
            </div>
          </CardBody>
        </Card>
      </div>
      
      {/* Charts Section */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-8">
        {/* Revenue by Client Tier Chart */}
        <Card>
          <CardHeader 
            title="Revenue by Client Tier" 
            actions={<Button variant="secondary" size="sm">View Details</Button>}
          />
          <CardBody className="p-0">
            <div className="aspect-w-16 aspect-h-9 bg-gray-100 rounded-lg flex items-center justify-center h-64">
              <div className="text-gray-400 flex flex-col items-center">
                <svg className="h-12 w-12 mb-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                </svg>
                <p>Chart visualization will appear here</p>
              </div>
            </div>
          </CardBody>
        </Card>
        
        {/* Fee Distribution Chart */}
        <Card>
          <CardHeader 
            title="Fee Distribution" 
            actions={<Button variant="secondary" size="sm">View Details</Button>}
          />
          <CardBody className="p-0">
            <div className="aspect-w-16 aspect-h-9 bg-gray-100 rounded-lg flex items-center justify-center h-64">
              <div className="text-gray-400 flex flex-col items-center">
                <svg className="h-12 w-12 mb-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M16 8v8m-4-5v5m-4-2v2m-2 4h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
                <p>Chart visualization will appear here</p>
              </div>
            </div>
          </CardBody>
        </Card>
      </div>
      
      {/* Top Clients Section */}
      <Card className="mb-8">
        <CardHeader title="Top Clients by Revenue" />
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Client Name</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Location</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">AUM (CAD)</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Fee Amount</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Effective Rate</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {[1, 2, 3, 4, 5].map((item) => (
                <tr key={item} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">Client {item}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">Toronto, ON</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${(Math.random() * 10000000).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,')}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${(Math.random() * 100000).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,')}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{(Math.random() * 1.5).toFixed(2)}%</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    <Badge color={Math.random() > 0.5 ? 'green' : 'blue'}>
                      {Math.random() > 0.5 ? 'Active' : 'New'}
                    </Badge>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </Card>
    </div>
  );
}