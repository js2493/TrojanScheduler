import React, { useState, useContext } from 'react';
import ClassSelector from './SelectClass';
import Calendar from './Calendar';
import LoginPopup from './LoginPopup';
import { AuthContext } from './AuthContext'; // Import AuthProvider

import "./Body.css"


function Body() {
    

    const { user, logout } = useContext(AuthContext); 
    const [showCalendar, setShowCalendar] = useState(false); // State to toggle between calendar and search
    const [isPopupOpen, setIsPopupOpen] = useState(false); // State to toggle whether login popup is open

    const togglePopup = () => {
        setIsPopupOpen(!isPopupOpen);
    };

    // Function to toggle between calendar and search
    const toggleView = () => {
        console.log(user);
        setShowCalendar(!showCalendar);
    };

    async function handleLogout() {
        try {
            const response = await fetch('http://localhost:8080/api/users/logout', {
                method: 'GET',
                credentials: 'include', 
            });

            if (response.ok) {
                window.location.href = '/';
            } else {
                console.error('Logout failed');
            }
        } catch (error) {
            console.error('Error logging out:', error);
        }
        logout()
    }
        
    const loginButton = (
        <div>
            <button onClick={togglePopup} className="login-button">
                        Log In / Sign Up
            </button>
            <LoginPopup isOpen={isPopupOpen} togglePopup={togglePopup} />
        </div>
    );

    const logoutButton = (
        <div>
            <button onClick={handleLogout} className="logout-button">
                    Log Out
            </button>
            Hello {user && user.username ? user.username : "monkey"}
        </div>
    )

    return (
        <div id="body-outer">
            <button className="toggle-button" onClick={toggleView}>
                    {showCalendar ? 'Show Course Search' : 'Show Calendars'}
            </button>
            {user ? logoutButton : loginButton }
            <div id="body-inner">
                <div className={`calendar ${showCalendar ? '' : 'hidden-mobile'}`}>
                    <Calendar />
                </div>
                <div className={`class-selector ${showCalendar ? 'hidden-mobile' : ''}`}>
                    <ClassSelector />
                </div>
            </div>
        </div>
    ) 
}

export default Body;
