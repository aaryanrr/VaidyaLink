import React, {useEffect, useState} from 'react';

import logo from '../../assets/Logo.png';
import '../css/CurrentAccess.css';
import {UserRedirectToInstitutionDashboard} from "../Utils";

const CurrentAccess = () => {
    const [accessList, setAccessList] = useState([]);
    const [loading, setLoading] = useState(true);
    const toHome = UserRedirectToInstitutionDashboard();

    useEffect(() => {
        const fetchAccess = async () => {
            const token = localStorage.getItem('token');
            const response = await fetch('/api/institutions/current-access', {
                headers: {'Authorization': `Bearer ${token}`}
            });
            if (response.ok) {
                const data = await response.json();
                setAccessList(data);
            }
            setLoading(false);
        };
        fetchAccess().then(r => console.log(r));
    }, []);

    return (
        <div className="current-access-container">
            <img src={logo} alt="VaidyaLink Logo" className="current-access-logo" onClick={toHome}/>
            <h1 className="current-access-title">Current Access</h1>
            <div className="current-access-table-container">
                <table className="current-access-table">
                    <thead>
                    <tr>
                        <th>User Email ID</th>
                        <th>Request ID</th>
                        <th>Approved</th>
                        <th>Time Period</th>
                        <th>View/Edit Data</th>
                    </tr>
                    </thead>
                    <tbody>
                    {loading ? (
                        <tr>
                            <td colSpan="5" className="current-access-loading">Loading...</td>
                        </tr>
                    ) : accessList.length === 0 ? (
                        <tr>
                            <td colSpan="5" className="current-access-loading">No records found.</td>
                        </tr>
                    ) : (
                        accessList.map((item, idx) => (
                            <tr key={item.requestId + idx}>
                                <td>{item.userEmail}</td>
                                <td>{item.requestId}</td>
                                <td>{item.approved ? "True" : "False"}</td>
                                <td>{item.timePeriod}</td>
                                <td>
                                    {item.actionRequested.includes("Read") && (
                                        <a href={item.basicDataLink} className="current-access-link">Basic Data</a>
                                    )}
                                    {item.actionRequested.includes("Medical") && (
                                        <a href={item.medicalReportsLink} className="current-access-link">Medical
                                            Reports</a>
                                    )}
                                </td>
                            </tr>
                        ))
                    )}
                    </tbody>
                </table>
            </div>
            <footer className="current-access-footer">
                &copy; Copyright {new Date().getFullYear()} | VaidyaLink
            </footer>
        </div>
    );
};

export default CurrentAccess;
