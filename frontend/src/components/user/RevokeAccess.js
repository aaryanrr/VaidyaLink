import React, {useState} from 'react';

import logo from '../../assets/Logo.png';
import '../css/RevokeAccessUser.css';
import {UserRedirectToUserDashboard} from "../Utils";

const RevokeAccess = () => {
    const [accessId, setAccessId] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const toHome = UserRedirectToUserDashboard();

    const handleRevoke = async (e) => {
        e.preventDefault();
        setErrorMessage('');
        setSuccessMessage('');
        if (!accessId || !password) {
            setErrorMessage('Both fields are required.');
            return;
        }
        const token = localStorage.getItem('token');
        const encodedPassword = btoa(password);
        const encodedId = btoa(accessId);
        const response = await fetch('/api/users/revoke-access', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({accessRequestId: encodedId, password: encodedPassword})
        });
        if (response.ok) {
            setSuccessMessage('Access revoked successfully.');
            setErrorMessage('');
        } else {
            const msg = await response.text();
            setErrorMessage(msg || 'Failed to revoke access.');
            setSuccessMessage('');
        }
    };

    return (
        <div className="revoke-container">
            <img src={logo} alt="VaidyaLink Logo" className="revoke-logo" onClick={toHome}/>
            <h1 className="revoke-title">Revoke Access</h1>
            <form className="revoke-form" onSubmit={handleRevoke}>
                <input
                    type="text"
                    placeholder="Enter access request ID"
                    value={accessId}
                    onChange={e => setAccessId(e.target.value)}
                    className="revoke-input"
                />
                <input
                    type="password"
                    placeholder="Enter your current password"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    className="revoke-input"
                />
                <button type="submit" className="revoke-button">Revoke Access</button>
                {successMessage && <div className="success-message">{successMessage}</div>}
                {errorMessage && <div className="error-message">{errorMessage}</div>}
            </form>
            <footer className="revoke-footer">
                &copy; Copyright {new Date().getFullYear()} | VaidyaLink
            </footer>
        </div>
    );
};

export default RevokeAccess;
