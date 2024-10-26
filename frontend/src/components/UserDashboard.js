import React from 'react';
import './css/UserDashboard.css';
import logo from '../assets/Logo.png';
import {useNavigate} from 'react-router-dom';

const UserDashboard = () => {
    const navigate = useNavigate();

    const handleNavigation = (path) => {
        navigate(path);
    };

    return (
        <div className="dashboard-container">
            <img src={logo} alt="VaidyaLink Logo" className="dashboard-logo"/>
            <h1 className="dashboard-title">User Dashboard</h1>
            <div className="dashboard-options">
                <div className="dashboard-option" onClick={() => handleNavigation('/current-access')}>
                    <span>Current access</span>
                    <span className="arrow">→</span>
                </div>
                <div className="dashboard-option" onClick={() => handleNavigation('/access-history')}>
                    <span>Access history</span>
                    <span className="arrow">→</span>
                </div>
                <div className="dashboard-option" onClick={() => handleNavigation('/view-records')}>
                    <span>View your records</span>
                    <span className="arrow">→</span>
                </div>
            </div>
            <footer className="dashboard-footer">
                &copy; Copyright 2024 | VaidyaLink
            </footer>
        </div>
    );
};

export default UserDashboard;
