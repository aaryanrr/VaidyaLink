import React, {useState} from 'react';
import './css/UserLogin.css';
import logo from '../assets/Logo.png';

function UserLogin() {
    const [aadhaar, setAadhaar] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('/api/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({aadhaar, password}),
            });

            if (response.ok) {
                const data = await response.json();
                setSuccessMessage('Login successful!');
                setErrorMessage('');
                localStorage.setItem('token', data.token);
            } else {
                const errorData = await response.json();
                setErrorMessage(errorData.message || 'Invalid Aadhaar or password.');
                setSuccessMessage('');
            }
        } catch (error) {
            setErrorMessage('Error: ' + error.message);
            setSuccessMessage('');
        }
    };

    return (
        <div className="login-container">
            <img src={logo} alt="VaidyaLink Logo" className="login-logo"/>
            <h1>User Login</h1>
            <form className="login-form" onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Aadhaar No."
                    value={aadhaar}
                    onChange={(e) => setAadhaar(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <button type="submit" className="login-button">Login</button>
            </form>

            {successMessage && <p className="success-message">{successMessage}</p>}
            {errorMessage && <p className="error-message">{errorMessage}</p>}

            <footer className="userlogin-footer">
                &copy; Copyright 2024 | VaidyaLink
            </footer>
        </div>
    );
}

export default UserLogin;
