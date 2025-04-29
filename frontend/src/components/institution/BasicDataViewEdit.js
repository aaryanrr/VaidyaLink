import React, {useEffect, useState} from 'react';

import logo from '../../assets/Logo.png';
import '../css/BasicDataViewEdit.css';
import {UserRedirectToInstitutionDashboard} from "../Utils";

const BasicDataViewEdit = () => {
    const [data, setData] = useState(null);
    const [editMode, setEditMode] = useState(false);
    const [formData, setFormData] = useState({});
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [loading, setLoading] = useState(true);
    const [canEdit, setCanEdit] = useState(false);
    const toHome = UserRedirectToInstitutionDashboard();

    useEffect(() => {
        const params = new URLSearchParams(window.location.search);
        const accessRequestId = params.get('id');
        if (!accessRequestId) {
            setError('Missing access request ID.');
            setLoading(false);
            return;
        }
        const accessKey = window.prompt('Enter your access key:');
        if (!accessKey) {
            setError('Access key required.');
            setLoading(false);
            return;
        }
        const fetchData = async () => {
            const token = localStorage.getItem('token');
            const response = await fetch('/api/institutions/basic-data', {
                method: 'POST',
                headers: {'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json'},
                body: JSON.stringify({accessRequestId, accessKey})
            });
            if (response.ok) {
                const d = await response.json();
                setData(d);
                setFormData(d);
                setCanEdit(d.canEdit);
            } else {
                const msg = await response.text();
                setError(msg || 'Failed to fetch data.');
            }
            setLoading(false);
        };
        fetchData().then(r => console.log(r));
    }, []);

    const handleEdit = () => {
        if (canEdit) setEditMode(true);
    };

    const handleChange = (e) => setFormData({...formData, [e.target.name]: e.target.value});

    const handleSave = async () => {
        const params = new URLSearchParams(window.location.search);
        const accessRequestId = params.get('id');
        const accessKey = window.prompt('Enter your access key:');
        if (!accessKey) {
            setError('Access key required.');
            return;
        }
        const token = localStorage.getItem('token');
        const response = await fetch('/api/institutions/basic-data', {
            method: 'POST',
            headers: {'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json'},
            body: JSON.stringify({
                accessRequestId,
                accessKey,
                edit: "true",
                editData: {
                    phoneNumber: formData.phoneNumber,
                    address: formData.address,
                    bloodGroup: formData.bloodGroup,
                    emergencyContact: formData.emergencyContact,
                    allergies: formData.allergies,
                    heightCm: formData.heightCm,
                    weightKg: formData.weightKg
                }
            })
        });
        if (response.ok) {
            setSuccess('Data updated successfully.');
            setEditMode(false);
        } else {
            const msg = await response.text();
            setError(msg || 'Failed to update data.');
        }
    };

    const handleBack = () => window.location.href = '/current-access';

    if (loading) {
        return (
            <div className="basicdata-container">
                <img src={logo} alt="VaidyaLink Logo" className="basicdata-logo"/>
                <h1 className="basicdata-title">Basic Data</h1>
                <div className="basicdata-loading">Loading...</div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="basicdata-container">
                <img src={logo} alt="VaidyaLink Logo" className="basicdata-logo"/>
                <h1 className="basicdata-title">Basic Data</h1>
                <div className="basicdata-error">{error}</div>
            </div>
        );
    }

    return (
        <div className="basicdata-container">
            <img src={logo} alt="VaidyaLink Logo" className="basicdata-logo" onClick={toHome}/>
            <h1 className="basicdata-title">Basic Data</h1>
            <form className="basicdata-form">
                <div className="basicdata-row">
                    <label>Name:</label>
                    <input type="text" value={data.name} disabled/>
                </div>
                <div className="basicdata-row">
                    <label>Email:</label>
                    <input type="text" value={data.email} disabled/>
                </div>
                <div className="basicdata-row">
                    <label>Phone:</label>
                    <input type="text" name="phoneNumber" value={formData.phoneNumber || ''} onChange={handleChange}
                           disabled={!editMode}/>
                </div>
                <div className="basicdata-row">
                    <label>DOB:</label>
                    <input type="text" value={data.dateOfBirth} disabled/>
                </div>
                <div className="basicdata-row">
                    <label>Address:</label>
                    <input type="text" name="address" value={formData.address || ''} onChange={handleChange}
                           disabled={!editMode}/>
                </div>
                <div className="basicdata-row">
                    <label>Blood Group:</label>
                    <input type="text" name="bloodGroup" value={formData.bloodGroup || ''} onChange={handleChange}
                           disabled={!editMode}/>
                </div>
                <div className="basicdata-row">
                    <label>Emergency Contact:</label>
                    <input type="text" name="emergencyContact" value={formData.emergencyContact || ''}
                           onChange={handleChange} disabled={!editMode}/>
                </div>
                <div className="basicdata-row">
                    <label>Allergies:</label>
                    <input type="text" name="allergies" value={formData.allergies || ''} onChange={handleChange}
                           disabled={!editMode}/>
                </div>
                <div className="basicdata-row">
                    <label>Height (cm):</label>
                    <input type="text" name="heightCm" value={formData.heightCm || ''} onChange={handleChange}
                           disabled={!editMode}/>
                </div>
                <div className="basicdata-row">
                    <label>Weight (kg):</label>
                    <input type="text" name="weightKg" value={formData.weightKg || ''} onChange={handleChange}
                           disabled={!editMode}/>
                </div>
                <div className="basicdata-buttons">
                    <button type="button" className="basicdata-back" onClick={handleBack}>Back</button>
                    {!editMode && canEdit &&
                        <button type="button" className="basicdata-edit" onClick={handleEdit}>Edit</button>}
                    {editMode && canEdit &&
                        <button type="button" className="basicdata-save" onClick={handleSave}>Save</button>}
                </div>
                {success && <div className="basicdata-success">{success}</div>}
                {error && <div className="basicdata-error">{error}</div>}
            </form>
            <footer className="basicdata-footer">
                &copy; Copyright {new Date().getFullYear()} | VaidyaLink
            </footer>
        </div>
    );
};

export default BasicDataViewEdit;
