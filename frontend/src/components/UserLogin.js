import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';

const validateToken = async (token) => {
    const response = await fetch('/api/users/validate-token', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
        },
    });
    return response.ok;
};

function UserLogin() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            validateToken(token).then(isValid => {
                if (isValid) navigate('/user-dashboard');
            });
        }
    }, [navigate]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('/api/users/login', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({email, password}),
            });
            if (response.ok) {
                const {token} = await response.json();
                localStorage.setItem('token', token);
                navigate('/user-dashboard');
            } else {
                setErrorMessage('Invalid email or password.');
            }
        } catch (error) {
            setErrorMessage(`Error: ${error.message}`);
        }
    };


    return (
        <div className="login-container">
            <img src={logo} alt="VaidyaLink Logo" className="login-logo"/>
            <h1>User Login</h1>
            <form className="login-form" onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Email Address"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
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
