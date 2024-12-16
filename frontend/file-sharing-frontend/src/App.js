import './App.css';
//Routing
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';

import 'bootstrap/dist/css/bootstrap.min.css';

import React, { useState, useEffect } from 'react';

// Pages
import HomeAdmin from './pages/Admin/HomeAdmin';
import LoginAdmin from './pages/Admin/LoginAdmin';
import HomeUser from './pages/User/HomeUser';
import LoginRegisterUser from './pages/User/LoginRegisterUser';

function App() {
  // State for dark mode
  const [isDarkMode, setIsDarkMode] = useState(false);

  // Function to update dark mode state
  const updateDarkModeFromLocalStorage = () => {
    const storedPreference = localStorage.getItem('isDarkMode');
    setIsDarkMode(storedPreference === 'true');
  };

  useEffect(() => {
    updateDarkModeFromLocalStorage();

    const handleStorageChange = (event) => {
      if (event.key === 'isDarkMode') {
        updateDarkModeFromLocalStorage();
      }
    };

    window.addEventListener('storage', handleStorageChange);

    const handleCustomEvent = () => {
      updateDarkModeFromLocalStorage();
    };
    window.addEventListener('darkModeUpdated', handleCustomEvent);

    return () => {
      window.removeEventListener('storage', handleStorageChange);
      window.removeEventListener('darkModeUpdated', handleCustomEvent);
    };
  }, []);

  return (
    <div className={`${isDarkMode ? 'bg-dark text-light' : 'bg-light text-dark'} min-vh-100`}>
      <Router>
        <Routes>
          <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/login" element={<LoginRegisterUser />} />
          <Route path="/home" element={<HomeUser />} />
          <Route path="/login_admin" element={<LoginAdmin />} />
          <Route path="/home_admin" element={<HomeAdmin />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
