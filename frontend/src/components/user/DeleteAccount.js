import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';

import logo from '../../assets/Logo.png';
import '../css/DeleteAccountUser.css';
import {UserRedirectToUserDashboard} from "../Utils";

const DeleteAccount = () => {
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();
    const toHome = UserRedirectToUserDashboard();

    const handleDelete = async (e) => {
        e.preventDefault();
        if (!password) {
            setErrorMessage('Password is required.');
            return;
        }
        if (!window.confirm("This action is irreversible and once the account is deleted it cannot be recovered.")) {
            return;
        }
        const token = localStorage.getItem('token');
        const encodedPassword = btoa(password);
        const response = await fetch('/api/users/delete-account', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({password: encodedPassword})
        });
        if (response.ok) {
            localStorage.removeItem('token');
            navigate('/');
        } else {
            setErrorMessage('Incorrect Password');
        }
    };

    return (
        <div className="delete-container">
            <img src={logo} alt="VaidyaLink Logo" className="delete-logo" onClick={toHome}/>
            <h1 className="delete-title">Delete Account & Data</h1>
            <form className="delete-form" onSubmit={handleDelete}>
                <input
                    type="password"
                    placeholder="Enter your current password"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    className="delete-input"
                />
                <button type="submit" className="delete-button">Delete</button>
                {errorMessage && <div className="delete-error">{errorMessage}</div>}
            </form>
            <footer className="delete-footer">
                &copy; Copyright {new Date().getFullYear()} | VaidyaLink
            </footer>
        </div>
    );
};

export default DeleteAccount;
