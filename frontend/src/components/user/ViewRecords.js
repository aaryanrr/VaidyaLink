import React, {useEffect, useState} from 'react';

import '../css/ViewRecords.css';
import logo from '../../assets/Logo.png';
import {UserRedirectToUserDashboard} from "../Utils";

const ViewRecords = () => {
    const toHome = UserRedirectToUserDashboard();
    const [records, setRecords] = useState(null);
    const [decrypted, setDecrypted] = useState(null);

    useEffect(() => {
        const fetchRecords = async () => {
            const token = localStorage.getItem('token');
            if (!token) {
                alert('Not authenticated!');
                return;
            }

            const password = window.prompt('Enter your password to decrypt your records:');
            if (!password) {
                alert('Password is required to view records.');
                return;
            }
            const response = await fetch('http://localhost:8080/api/users/records', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'X-Password': btoa(password)
                }
            });
            if (response.ok) {
                const data = await response.json();
                setRecords(data);
            } else {
                alert('Failed to fetch records.');
            }
        };
        fetchRecords().then(r => console.log(r)).catch(e => console.error(e));
    }, []);

    useEffect(() => {
        if (records && !decrypted) {
            setDecrypted({
                email: records.email,
                phoneNumber: records.phoneNumber,
                dateOfBirth: records.dateOfBirth,
                address: records.address,
                bloodGroup: records.bloodGroup,
                emergencyContact: records.emergencyContact,
                allergies: records.allergies,
                heightCm: records.heightCm,
                weightKg: records.weightKg,
            });
        }
    }, [records, decrypted]);

    useEffect(() => {
        if (decrypted) {
            const timer = setTimeout(() => {
                toHome();
            }, 5000);
            return () => clearTimeout(timer);
        }
    }, [decrypted]);

    if (!records || !decrypted) {
        return (
            <div className="records-container">
                <img src={logo} alt="VaidyaLink Logo" className="records-logo"/>
                <h1 className="records-title">Your Records</h1>
                <div className="records-loading">Loading...</div>
            </div>
        );
    }

    return (
        <div className="records-container">
            <img src={logo} alt="VaidyaLink Logo" className="records-logo" onClick={toHome}/>
            <h1 className="records-title">Your Records</h1>
            <div className="records-content">
                <div className="records-col">
                    <div className="records-row"><span className="records-label">Email:</span> {decrypted.email}</div>
                    <div className="records-row"><span
                        className="records-label">Phone Number:</span> {decrypted.phoneNumber}</div>
                    <div className="records-row"><span
                        className="records-label">Date of Birth:</span> {decrypted.dateOfBirth}</div>
                    <div className="records-row"><span className="records-label">Address:</span> {decrypted.address}
                    </div>
                    <div className="records-row"><span
                        className="records-label">Blood Group:</span> {decrypted.bloodGroup}</div>
                    <div className="records-row"><span
                        className="records-label">Emergency Contact:</span> {decrypted.emergencyContact}</div>
                </div>
                <div className="records-col">
                    <div className="records-row"><span className="records-label">Allergies:</span> {decrypted.allergies}
                    </div>
                    <div className="records-row"><span
                        className="records-label">Height (In cm):</span> {decrypted.heightCm}</div>
                    <div className="records-row"><span
                        className="records-label">Weight (In kg):</span> {decrypted.weightKg}</div>
                </div>
            </div>
            <footer className="records-footer">
                &copy; Copyright {new Date().getFullYear()} | VaidyaLink
            </footer>
        </div>
    );
};

export default ViewRecords;
