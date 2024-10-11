import React from 'react';
import {Link} from 'react-router-dom';
import './css/NotFound.css';

function NotFound() {
    return (
        <div className="error-container">
            <div className="error-content">
                <h1>404</h1>
                <h2>Something’s wrong here</h2>
                <p>Might have to visit a doctor...</p>
                <Link to="/" className="error-button">
                    Go to home <span className="arrow">→</span>
                </Link>
            </div>
            <footer className="error-footer">
                <p>© Copyright 2024 | VaidyaLink</p>
            </footer>
        </div>
    );
}

export default NotFound;
