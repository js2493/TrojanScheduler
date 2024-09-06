import React from 'react';
import './App.css';
import Body from './Body';
import { AuthProvider } from './AuthContext'; // Import AuthProvider

function App() {
    return  (
        <AuthProvider>
            <div className="App">
                <Body />
            </div>
        </AuthProvider>
    ) 
}

export default App;
