import React, { useState } from 'react';
import './App.css';
import ClassSelector from './SelectClass';
import LoginPopup from './LoginPopup';

function App() {
    const [isPopupOpen, setIsPopupOpen] = useState(false);
    const [username, setUsername] = useState('');
    const [isLoggedIn, setLoggedIn] = useState(false);

    const togglePopup = () => {
        setIsPopupOpen(!isPopupOpen);
    };

    const handleLoginSuccess = (username) => {
        setLoggedIn(true)
        setUsername(username);
    };

    async function handleLogout() {
        try {
            const response = await fetch('http://localhost:8080/api/users/logout', {
                method: 'GET',
                credentials: 'include', // Include credentials (cookies) in the request
            });

            if (response.ok) {
                window.location.href = '/';
            } else {
                console.error('Logout failed');
            }
        } catch (error) {
            console.error('Error logging out:', error);
        }
    }
        
    
    return isLoggedIn ? (
        <div className="App">
            <button onClick={handleLogout} className="logout-button">
                Log Out
            </button>
            Hello, {username}
            <ClassSelector />
        </div>
    ) : (
        <div className="App">
            <button onClick={togglePopup} className="login-button">
                Log In / Sign Up
            </button>
            <LoginPopup isOpen={isPopupOpen} togglePopup={togglePopup} onLoginSuccess={handleLoginSuccess} />
            <ClassSelector />
        </div>
    ) ;
}

export default App;
