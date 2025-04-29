import React, {useState, useEffect} from 'react';

import logo from '../assets/Logo.png';
import '../components/css/SyncUpdate.css';
import {UserRedirectToHome} from "./Utils";

const SyncUpdate = () => {
    const [accessKey, setAccessKey] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [loading, setLoading] = useState(false);
    const [accessRequestId, setAccessRequestId] = useState('');
    const toHome = UserRedirectToHome();

    useEffect(() => {
        const params = new URLSearchParams(window.location.search);
        const id = params.get('id');
        if (id) setAccessRequestId(id);
    }, []);

    const handleSync = async (e) => {
        e.preventDefault();
        setErrorMessage('');
        setSuccessMessage('');
        setLoading(true);
        if (!accessKey || !password || !accessRequestId) {
            setErrorMessage('Both fields are required.');
            setLoading(false);
            return;
        }
        const encodedAccessKey = btoa(accessKey);
        const encodedPassword = btoa(password);
        const response = await fetch('/api/sync/sync-approved', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                accessRequestId: btoa(accessRequestId),
                accessKey: encodedAccessKey,
                password: encodedPassword
            })
        });
        if (response.ok) {
            setSuccessMessage('Data sync complete.');
            setErrorMessage('');
        } else {
            const msg = await response.text();
            setErrorMessage(msg || 'Failed to sync data.');
            setSuccessMessage('');
        }
        setLoading(false);
    };

    return (
        <div className="sync-container">
            <img src={logo} alt="VaidyaLink Logo" className="sync-logo" onClick={toHome}/>
            <h1 className="sync-title">Approve Data Sync</h1>
            <form className="sync-form" onSubmit={handleSync}>
                <input
                    type="text"
                    placeholder="Enter institution access key"
                    value={accessKey}
                    onChange={e => setAccessKey(e.target.value)}
                    className="sync-input"
                />
                <input
                    type="password"
                    placeholder="Enter your current password"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    className="sync-input"
                />
                <button type="submit" className="sync-button" disabled={loading}>
                    {loading ? 'Syncing...' : 'Sync'}
                </button>
                {successMessage && <div className="success-message">{successMessage}</div>}
                {errorMessage && <div className="error-message">{errorMessage}</div>}
            </form>
            <footer className="sync-footer">
                &copy; Copyright {new Date().getFullYear()} | VaidyaLink
            </footer>
        </div>
    );
};

export default SyncUpdate;
