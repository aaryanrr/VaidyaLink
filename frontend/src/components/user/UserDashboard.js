import React from 'react';
import {useNavigate} from 'react-router-dom';

import '../css/UserDashboard.css';
import logo from '../../assets/Logo.png';
import {UserRedirectToHome} from "../Utils";

const UserDashboard = () => {
    const navigate = useNavigate();
    const toHome = UserRedirectToHome();

    const handleNavigation = (path) => {
        if (path === '/users') {
            localStorage.removeItem('token');
        }
        navigate(path);
    };
    const handleLogout = async () => {
        const token = localStorage.getItem('token');

        if (token) {
            try {
                await fetch('http://localhost:8080/api/users/logout', {
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
        navigate('/users');
    };

    return (
        <div className="dashboard-container">
            <img src={logo} alt="VaidyaLink Logo" className="dashboard-logo" onClick={toHome}/>
            <h1 className="dashboard-title">User Dashboard</h1>
            <div className="dashboard-options">
                <div className="dashboard-option" onClick={() => handleNavigation('/access-history')}>
                    <span>Access history</span>
                    <span className="arrow">→</span>
                </div>
                <div className="dashboard-option" onClick={() => handleNavigation('/view-records')}>
                    <span>View your records</span>
                    <span className="arrow">→</span>
                </div>
                <div className="dashboard-option" onClick={handleLogout}>
                    <span>Logout</span>
                    <span className="arrow">→</span>
                </div>
            </div>
            <footer className="dashboard-footer">
                &copy; Copyright {new Date().getFullYear()} | VaidyaLink
            </footer>
        </div>
    );
};

export default UserDashboard;
