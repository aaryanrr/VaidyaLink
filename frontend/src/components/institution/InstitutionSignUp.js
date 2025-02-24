import React, {useState} from 'react';

import '../css/InstitutionSignUp.css';
import logo from '../../assets/Logo.png';
import {UserRedirectToHome} from "../Utils";

function InstitutionSignUp() {
    const [institutionName, setInstitutionName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [licenseFile, setLicenseFile] = useState(null);
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const toHome = UserRedirectToHome();

    const handleFileChange = (e) => {
        setLicenseFile(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData();
        formData.append('institutionName', institutionName);
        formData.append('email', email);
        formData.append('password', password);
        formData.append('licenseFile', licenseFile);

        try {
            const response = await fetch('/api/institutions/register', {
                method: 'POST',
                body: formData,
            });

            if (response.ok) {
                setSuccessMessage('Institution registered successfully!');
                setErrorMessage('');
            } else {
                const contentType = response.headers.get("content-type");
                if (contentType && contentType.indexOf("application/json") !== -1) {
                    const errorData = await response.json();
                    setErrorMessage(errorData.message || 'Error occurred during registration.');
                } else {
                    const errorText = await response.text();
                    setErrorMessage(errorText || 'Error occurred during registration.');
                }
                setSuccessMessage('');
            }
        } catch (error) {
            setErrorMessage('Error: ' + error.message);
            setSuccessMessage('');
        }
    };


    return (
        <div className="signup-container">
            <img src={logo} alt="VaidyaLink Logo" className="signup-logo" onClick={toHome}/>
            <h1>Institution Sign Up</h1>
            <form className="signup-form" onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Institution Name"
                    value={institutionName}
                    onChange={(e) => setInstitutionName(e.target.value)}
                    required
                />
                <input
                    type="email"
                    placeholder="Email Address"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <input
                    type="file"
                    accept=".pdf,.doc,.docx"
                    onChange={handleFileChange}
                    required
                    className="file-input"
                />
                <button type="submit" className="signup-button">Sign up</button>
            </form>

            {successMessage && <p className="success-message">{successMessage}</p>}
            {errorMessage && <p className="error-message">{errorMessage}</p>}

            <p>Already have an account? <a href="/institution" className="already-signed-up">Login</a></p>
            <footer className="signup-footer">
                &copy; Copyright 2024 | VaidyaLink
            </footer>
        </div>
    );
}

export default InstitutionSignUp;
