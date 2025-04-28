import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';

import '../css/AccessRequest.css';
import logo from '../../assets/Logo.png';
import {UserRedirectToInstitutionDashboard} from "../Utils";

const AccessRequest = () => {
    const [aadhaar, setAadhaar] = useState('');
    const [dataCategory, setDataCategory] = useState([]);
    const [timePeriod, setTimePeriod] = useState('');
    const [actionRequired, setActionRequired] = useState([]);
    const navigate = useNavigate();
    const toHome = UserRedirectToInstitutionDashboard();

    const handleDataCategoryChange = (e) => {
        const options = Array.from(e.target.selectedOptions, option => option.value);
        setDataCategory(options);
    };

    const handleActionRequiredChange = (e) => {
        const options = Array.from(e.target.selectedOptions, option => option.value);
        setActionRequired(options);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('/api/access-requests/request', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                },
                body: JSON.stringify({
                    aadhaarNumber: aadhaar,
                    dataCategory,
                    timePeriod,
                    actionRequired,
                }),

            });

            if (response.ok) {
                alert("Access request submitted successfully!");
                navigate('/institution-dashboard');
            } else {
                alert("Failed to submit access request.");
            }
        } catch (error) {
            console.error("Error:", error);
        }
    };

    return (
        <div className="access-request-container">
            <img src={logo} alt="VaidyaLink Logo" className="access-request-logo" onClick={toHome}/>
            <h1>Access Request</h1>
            <form className="access-request-form" onSubmit={handleSubmit}>
                <label>Patient's Aadhaar No.:</label>
                <input
                    type="text"
                    value={aadhaar}
                    onChange={(e) => setAadhaar(e.target.value)}
                    required
                />

                <label>Data Category:</label>
                <select multiple value={dataCategory} onChange={handleDataCategoryChange}>
                    <option value="Basic Data">Basic Data</option>
                    <option value="Medical Reports">Medical Reports</option>
                </select>

                <label>Time Period:</label>
                <input
                    type="date"
                    value={timePeriod}
                    onChange={(e) => setTimePeriod(e.target.value)}
                    required
                />

                <label>Action Required:</label>
                <select multiple value={actionRequired} onChange={handleActionRequiredChange}>
                    <option value="Read">Read</option>
                    <option value="Write">Write</option>
                </select>

                <div className="button-group">
                    <button type="button" onClick={() => navigate(-1)} className="back-button">Back</button>
                    <button type="submit" className="request-button">Request</button>
                </div>
            </form>
            <footer className="access-request-footer">
                &copy; Copyright {new Date().getFullYear()} | VaidyaLink
            </footer>
        </div>
    );
};

export default AccessRequest;
