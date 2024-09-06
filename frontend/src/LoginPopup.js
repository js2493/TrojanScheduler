import React, { useState, useContext } from 'react';
import { AuthContext } from './AuthContext'; // Import the AuthContext
import './LoginPopup.css'; // Include the CSS styles

function LoginPopup({ isOpen, togglePopup }) {

    const BASE_URL = "http://localhost:8080";

    const { login } = useContext(AuthContext);

    const [isLogin, setIsLogin] = useState(true);
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [error, setError] = useState('');

    if (!isOpen) return null; // Don't render the popup if it isn't open

    const toggleMode = () => {
        setIsLogin(!isLogin);
        setError('');
    };

    const handleSubmit = async (e) => {
        
        e.preventDefault();
        setError('');
        try {
            const response = await fetch(BASE_URL + (isLogin ? "/api/users/login" : "/api/users/register"), {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username,
                    password,
                    // ...(isLogin ? {} : { email }), // Include email if registering
                }),
            });

            if (response.ok) {
                const userData = await response.json();
                login(userData); // Call login function from AuthContext
                togglePopup(); // Close the popup after successful login
            } else {
                const errorData = await response.json();
                setError(errorData.message || "Failed to authenticate");
                console.log("failed to authenticate")
            }
        } catch (err) {
            console.log(err.message)
            setError('An erorr occured. Please try again later');
        }
    }

    return (
        <div className="login-popup">
            <h2>{isLogin ? 'Login' : 'Create Account'}</h2>
            {error && <div className="error-message">{error}</div>}
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="username">Username:</label>
                    <input
                        type="text"
                        id="username"
                        name="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                {!isLogin && (
                    <div className="form-group">
                        <label htmlFor="email">Email (optional):</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                    </div>
                )}
                <button type="submit" className="popup-submit-button">
                    {isLogin ? 'Login' : 'Create Account'}
                </button>
                <button type="button" onClick={togglePopup} className="popup-close-button">
                    Close
                </button>
            </form>
            <p className="auth-toggle-text">
                <span onClick={toggleMode} className="auth-toggle-link">
                    {isLogin ? "Don't have an account? Create one" : 'Already have an account? Login'}
                </span>
            </p>
        </div>
    );
}

export default LoginPopup;
