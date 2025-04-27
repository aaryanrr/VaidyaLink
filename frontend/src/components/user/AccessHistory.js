import React, {useEffect, useState} from 'react';

import '../css/UserAccessHistory.css';
import logo from '../../assets/Logo.png';
import {UserRedirectToUserDashboard} from '../Utils';

const AccessHistory = () => {
    const toHome = UserRedirectToUserDashboard();
    const [history, setHistory] = useState([]);
    const [loading, setLoading] = useState(true);


    useEffect(() => {
        const fetchHistory = async () => {
            const token = localStorage.getItem('token');
            if (!token) {
                alert('Not authenticated!');
                return;
            }
            const response = await fetch('http://localhost:8080/api/users/access-requests', {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            if (response.ok) {
                const data = await response.json();
                setHistory(data);
            } else {
                alert('Failed to fetch access history.');
            }
            setLoading(false);
        };
        fetchHistory().then(r => console.log(r)).catch(e => console.error(e));
    }, []);

    return (
        <div className="history-container">
            <img src={logo} alt="VaidyaLink Logo" className="history-logo" onClick={toHome}/>
            <h1 className="history-title">Access History</h1>
            <div className="history-table-container">
                <table className="history-table">
                    <thead>
                    <tr>
                        <th>Request ID</th>
                        <th>Institution Name</th>
                        <th>Action Requested</th>
                        <th>Time Period</th>
                        <th>Approved</th>
                        <th>Requested At</th>
                    </tr>
                    </thead>
                    <tbody>
                    {loading ? (
                        <tr>
                            <td colSpan="6" className="history-loading">Loading...</td>
                        </tr>
                    ) : history.length === 0 ? (
                        <tr>
                            <td colSpan="6" className="history-loading">No records found.</td>
                        </tr>
                    ) : (
                        history.map((item, idx) => (
                            <tr key={item.id + idx}>
                                <td className="history-id">{item.id}</td>
                                <td>{item.institutionName}</td>
                                <td>{item.actionRequested}</td>
                                <td>{item.timePeriod}</td>
                                <td>{item.approved}</td>
                                <td>{item.requestedAt.replace('T', ' ').slice(0, 19)}</td>
                            </tr>
                        ))
                    )}
                    </tbody>
                </table>
            </div>
            <footer className="history-footer">
                &copy; Copyright {new Date().getFullYear()} | VaidyaLink
            </footer>
        </div>
    );
};

export default AccessHistory;
