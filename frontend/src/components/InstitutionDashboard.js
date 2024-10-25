import React from 'react';
import './css/InstitutionDashboard.css';
import {Link} from 'react-router-dom';
import logo from '../assets/Logo.png'; // Adjust the path if necessary

const InstitutionDashboard = () => {
    return (
        <div className="dashboard-container">
            <header className="dashboard-header">
                <img src={logo} alt="VaidyaLink Logo" className="dashboard-logo"/>
                <h1>Institution Dashboard</h1>
            </header>

            <div className="dashboard-options">
                <div className="dashboard-row">
                    <Link to="/request-access" className="dashboard-option">
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
                </div>
            </div>

            <footer className="dashboard-footer">
                &copy; Copyright 2024 | VaidyaLink
            </footer>
        </div>
    );
};

export default InstitutionDashboard;
