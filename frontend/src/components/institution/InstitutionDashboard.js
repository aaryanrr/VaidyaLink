import React from 'react';
import '../css/InstitutionDashboard.css';
import {Link, useNavigate} from 'react-router-dom';
import logo from '../../assets/Logo.png';

const InstitutionDashboard = () => {
    const navigate = useNavigate();
    const handleLogout = async () => {
        const token = localStorage.getItem('token');

        if (token) {
            try {
                await fetch('http://localhost:8080/api/institutions/logout', {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });
            } catch (error) {
                console.error('Error during logout:', error);
            }
        }
        localStorage.removeItem('token');
        navigate('/institution');
    };


    return (
        <div className="dashboard-container">
            <header className="dashboard-header">
                <img src={logo} alt="VaidyaLink Logo" className="dashboard-logo"/>
                <h1>Institution Dashboard</h1>
            </header>

            <div className="dashboard-options">
                <div className="dashboard-row">
                    <Link to="/access-request" className="dashboard-option">
                        Request access<span className="arrow">➜</span>
                    </Link>
                    <Link to="/current-access" className="dashboard-option">
                        Current access<span className="arrow">➜</span>
                    </Link>
                </div>

                <div className="dashboard-row">
                    <Link to="/invite-new-user" className="dashboard-option">
                        Invite new user<span className="arrow">➜</span>
                    </Link>
                    <Link to="/access-history" className="dashboard-option">
                        Access history<span className="arrow">➜</span>
                    </Link>
                </div>

                <div className="dashboard-row centered-row">
                    <Link to="/profile-settings" className="dashboard-option centered-option">
                        Profile Settings<span className="arrow">➜</span>
                    </Link>
                    <button onClick={handleLogout} className="dashboard-option logout-button">
                        Logout<span className="arrow">➜</span>
                    </button>
                </div>
            </div>

            <footer className="dashboard-footer">
                &copy; Copyright 2024 | VaidyaLink
            </footer>
        </div>
    );
};

export default InstitutionDashboard;
