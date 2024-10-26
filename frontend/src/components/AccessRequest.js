import React, {useState} from 'react';
import './css/AccessRequest.css';
import logo from '../assets/Logo.png';
import {useNavigate} from 'react-router-dom';

const AccessRequest = () => {
    const navigate = useNavigate();
    const [aadhaar, setAadhaar] = useState('');
    const [dataCategory, setDataCategory] = useState([]);
    const [timePeriod, setTimePeriod] = useState('');
    const [actionRequired, setActionRequired] = useState([]);

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic
    };

    const handleMultiSelect = (setState, value) => {
        setState((prev) => prev.includes(value) ? prev.filter(v => v !== value) : [...prev, value]);
    };

    return (
        <div className="access-container">
            <img src={logo} alt="VaidyaLink Logo" className="access-logo"/>
            <h1>Access Request</h1>
            <form className="access-form" onSubmit={handleSubmit}>
                <label>Patient's Aadhaar No.:</label>
                <input
                    type="text"
                    value={aadhaar}
                    onChange={(e) => setAadhaar(e.target.value)}
                    required
                />

                <label>Data Category:</label>
                <select multiple value={dataCategory}
                        onChange={(e) => handleMultiSelect(setDataCategory, e.target.value)}>
                    <option value="Basic Data">Basic Data</option>
                    <option value="Medical Reports">Medical Reports</option>
                </select>

                <label>Time Period:</label>
                <input
                    type="text"
                    value={timePeriod}
                    onChange={(e) => setTimePeriod(e.target.value)}
                    required
                />

                <label>Action Required:</label>
                <select multiple value={actionRequired}
                        onChange={(e) => handleMultiSelect(setActionRequired, e.target.value)}>
                    <option value="Read">Read</option>
                    <option value="Write">Write</option>
                </select>

                <div className="button-group">
                    <button type="button" onClick={() => navigate(-1)} className="back-button">Back</button>
                    <button type="submit" className="request-button">Request</button>
                </div>
            </form>
            <footer className="access-footer">
                &copy; Copyright 2024 | VaidyaLink
            </footer>
        </div>
    );
};

export default AccessRequest;
