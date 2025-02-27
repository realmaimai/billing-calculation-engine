import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { useState } from 'react';
import './App.css';
import Navbar from './components/layout/Navbar';
import Dashboard from './pages/Dashboard';
import Clients from './pages/Clients';
import Portfolios from './pages/Portfolios';
import Upload from './pages/Upload';

function App() {
  return (
    <Router>
      <div className="min-h-screen w-full bg-gray-100">
        <div className="w-full fixed top-0 left-0 right-0 z-50">
          <Navbar />
        </div>
        <main className="pt-20 px-4 w-full">
          <div className="max-w-7xl mx-auto">
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/clients" element={<Clients />} />
              <Route path="/portfolios" element={<Portfolios />} />
              <Route path="/upload" element={<Upload />} />
            </Routes>
          </div>
        </main>
      </div>
    </Router>
  );
}

export default App;