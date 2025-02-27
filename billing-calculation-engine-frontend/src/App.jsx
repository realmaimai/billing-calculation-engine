import { useState } from 'react'
import './App.css'
import Navbar from './components/Navbar';

function App() {
  return (
    // Remove any width constraints from the root element
    <div className="min-h-screen w-full bg-gray-100">
      {/* Make the navbar container full width with position fixed */}
      <div className="w-full fixed top-0 left-0 right-0 z-50">
        <Navbar />
      </div>
      
      {/* Add padding to prevent content from hiding under the navbar */}
      <main className="pt-16 px-4 w-full">
        {/* Your page content */}
        <div className="max-w-7xl mx-auto">
          {/* Page content can still be constrained if desired */}
        </div>
      </main>
    </div>
  );
}

export default App
