import React from 'react';

import './css/About.css';
import logo from '../assets/Logo.png';

function About() {
    return (
        <div className="about-container">
            <main className="about-main">
                <header className="about-header">
                    <img src={logo} className="about-logo" alt="VaidyaLink Logo"/>
                </header>
                <h1>VaidyaLink - About Us</h1>
                <p>VaidyaLink is a platform for Secure and Efficient storage of medical data.<br/>
                    Leveraging the Blockchain technology, the platform provides a decentralized<br/>
                    way to store patient data and provides the patient with granular control over their data.</p>
            </main>
        </div>
    );
}

export default About;
