import React from 'react';
import {Link} from 'react-router-dom'

import './css/Home.css';
import logo from '../assets/Logo.png';

const Home = () => {
    return (
        <div className="home-container">
            <main className="home-main">
                <header className="home-header">
                    <img src={logo} className="home-logo" alt="VaidyaLink Logo"/>
                </header>
                <h1>VaidyaLink</h1>
                <p>Secure Blockchain based Storage for Medical Data</p>
                <div className="home-buttons">
                    <Link to="/institution" className="button">Institutions</Link>
                    <Link to="/users" className="button">Users</Link>
                </div>
            </main>
            <footer className="home-footer">
                <p>© Copyright 2024 | VaidyaLink</p>
            </footer>
        </div>
    );
};

export default Home;
