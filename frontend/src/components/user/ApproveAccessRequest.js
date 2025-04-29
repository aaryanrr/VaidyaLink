import React, {useState, useEffect} from 'react';

import logo from '../../assets/Logo.png';
import '../css/ApproveAccessRequest.css';
import {UserRedirectToHome} from "../Utils";

const ApproveAccessRequest = () => {
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [loading, setLoading] = useState(false);
    const toHome = UserRedirectToHome();

    useEffect(() => {
        const params = new URLSearchParams(window.location.search);
        const accessId = params.get('id');
        if (accessId) {
            localStorage.setItem('access_id', accessId);
        }
    }, []);

    const handleApprove = async (e) => {
        e.preventDefault();
        setErrorMessage('');
        setSuccessMessage('');
        setLoading(true);
        const accessId = localStorage.getItem('access_id');
        if (!accessId || !password) {
            setErrorMessage('Access ID and password are required.');
            setLoading(false);
            return;
        }
        const encodedPassword = btoa(password);
        const encodedId = btoa(accessId);
        const response = await fetch('/api/access-requests/approve-access', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({accessRequestId: encodedId, password: encodedPassword})
        });
        if (response.ok) {
            setSuccessMessage('Access Request Approved Successfully.');
            setErrorMessage('');
        } else {
            const msg = await response.text();
            setErrorMessage(msg || 'Failed to approve access request.');
            setSuccessMessage('');
        }
        setLoading(false);
    };

    return (
        <div className="approve-container">
            <img src={logo} alt="VaidyaLink Logo" className="approve-logo" onClick={toHome}/>
            <h1 className="approve-title">Approve Access Request</h1>
            <form className="approve-form" onSubmit={handleApprove}>
                <input
                    type="password"
                    placeholder="Enter your password"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    className="approve-input"
                />
                <button type="submit" className="approve-button" disabled={loading}>
                    {loading ? 'Processing...' : 'Approve'}
                </button>
                {successMessage && <div className="approve-success">{successMessage}</div>}
                {errorMessage && <div className="approve-error">{errorMessage}</div>}
            </form>
            <footer className="approve-footer">
                &copy; Copyright {new Date().getFullYear()} | VaidyaLink
            </footer>
        </div>
    );
};

export default ApproveAccessRequest;
