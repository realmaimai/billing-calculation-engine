import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router';
import {useState, useEffect, ReactNode} from 'react';
import './App.css';
import Navbar from './components/layout/Navbar';
import Dashboard from './pages/Dashboard';
import Clients from './pages/Clients';
import Portfolios from './pages/Portfolios';
import Upload from './pages/Upload';
import Login from './pages/Login';
import Register from './pages/Register';
import authService from './services/authService';

interface ProtectedRouteProps {
  children: ReactNode;
}

// Protected route
const ProtectedRoute = ({ children }: ProtectedRouteProps) => {
  const isAuthenticated = authService.isAuthenticated();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // Check authentication status on component mount
  useEffect(() => {
    const checkAuth = () => {
      const authStatus = authService.isAuthenticated();
      setIsAuthenticated(authStatus);
    };

    checkAuth();

    // Set up listener for storage changes (for multi-tab logout)
    window.addEventListener('storage', checkAuth);

    return () => {
      window.removeEventListener('storage', checkAuth);
    };
  }, []);

  // Handle logout
  const handleLogout = () => {
    authService.logout();
    setIsAuthenticated(false);
  };

  return (
    <Router>
      <div className="min-h-screen w-full bg-gray-100">
        {isAuthenticated && (
          <div className="w-full fixed top-0 left-0 right-0 z-50">
            <Navbar onLogout={handleLogout} />
          </div>
        )}

        <main className={isAuthenticated ? "pt-20 px-4 w-full" : "w-full"}>
          <div className={isAuthenticated ? "max-w-7xl mx-auto" : ""}>
            <Routes>
              {/* Public routes */}
              <Route path="/login" element={<Login onLoginSuccess={() => setIsAuthenticated(true)} />} />
              <Route path="/register" element={<Register />} />

              {/* Protected routes */}
              <Route path="/" element={
                <ProtectedRoute>
                  <Dashboard />
                </ProtectedRoute>
              } />
              <Route path="/clients" element={
                <ProtectedRoute>
                  <Clients />
                </ProtectedRoute>
              } />
              <Route path="/portfolios" element={
                <ProtectedRoute>
                  <Portfolios />
                </ProtectedRoute>
              } />
              <Route path="/upload" element={
                <ProtectedRoute>
                  <Upload />
                </ProtectedRoute>
              } />

              {/* Redirect to login page for any unknown routes */}
              <Route path="*" element={<Navigate to={isAuthenticated ? "/" : "/login"} replace />} />
            </Routes>
          </div>
        </main>
      </div>
    </Router>
  );
}

export default App;