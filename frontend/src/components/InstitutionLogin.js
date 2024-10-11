import React, {useState} from 'react';
import {Link} from 'react-router-dom';
import './css/InstitutionLogin.css';
import logo from '../assets/Logo.png';

function Institution() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('/api/institutions/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({email, password}),
            });

            if (response.ok) {
                const contentType = response.headers.get('content-type');
                if (contentType && contentType.includes('application/json')) {
                    const data = await response.json();
                    setSuccessMessage('Login successful!');
                    localStorage.setItem('token', data.token);
                } else {
                    setSuccessMessage('Login successful!');
                }
                setErrorMessage('');
            } else {
                const errorData = await response.json();
                setErrorMessage(errorData.message || 'Invalid email or password.');
                setSuccessMessage('');
            }
        } catch (error) {
            setErrorMessage('Error: ' + error.message);
            setSuccessMessage('');
        }
    };

    return (
        <div className="institution-container">
            <div className="institution-header">
                <img src={logo} alt="Logo" className="institution-logo"/>
                <h1>Institution Login</h1>
            </div>
            <form className="institution-form" onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Email Address"
                    className="institution-input"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="Password"
                    className="institution-input"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <button className="institution-button" type="submit">Login</button>
                {successMessage && <p className="success-message">{successMessage}</p>}
                {errorMessage && <p className="error-message">{errorMessage}</p>}
                <p className="institution-signup">
                    Don’t have an account yet? <Link to="/signup">Sign Up</Link>
                </p>
            </form>
            <footer className="institution-footer">
                &copy; Copyright 2024 | VaidyaLink
            </footer>
        </div>
    );
}

export default Institution;
