import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';

import '../css/InviteUser.css';

const InviteUser = () => {
    const [formData, setFormData] = useState({
        name: '',
        aadhaar: '',
        email: '',
        dateOfBirth: '',
        phoneNumber: '',
        emergencyContact: '',
        bloodGroup: '',
        heightCm: '',
        weightKg: '',
        allergies: '',
        address: '',
    });
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        const {name, value} = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('http://localhost:8080/api/users/invite', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                },
                body: JSON.stringify(formData),
            });

            if (response.ok) {
                setSuccessMessage('User invited successfully!');
                setErrorMessage('');
            } else {
                setErrorMessage('Failed to invite user.');
            }
        } catch (error) {
            setErrorMessage('An error occurred: ' + error.message);
        }
    };

    return (
        <div className="invite-container">
            <h1>Invite New User</h1>
            <form className="invite-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <div className="form-group">
                        <label>Name:</label>
                        <input
                            type="text"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            placeholder="Enter patient's name"
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Blood Group:</label>
                        <input
                            type="text"
                            name="bloodGroup"
                            value={formData.bloodGroup}
                            onChange={handleChange}
                            placeholder="Enter blood group"
                        />
                    </div>
                </div>
                <div className="form-row">
                    <div className="form-group">
                        <label>Aadhaar No.:</label>
                        <input
                            type="text"
                            name="aadhaar"
                            value={formData.aadhaar}
                            onChange={handleChange}
                            placeholder="Enter Aadhaar number"
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Height (cm):</label>
                        <input
                            type="number"
                            name="heightCm"
                            value={formData.heightCm}
                            onChange={handleChange}
                            placeholder="Enter height in cm"
                        />
                    </div>
                </div>
                <div className="form-row">
                    <div className="form-group">
                        <label>Email Address:</label>
                        <input
                            type="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            placeholder="Enter patient's email address"
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Weight (kg):</label>
                        <input
                            type="number"
                            name="weightKg"
                            value={formData.weightKg}
                            onChange={handleChange}
                            placeholder="Enter weight in kg"
                        />
                    </div>
                </div>
                <div className="form-row">
                    <div className="form-group">
                        <label>Date of Birth:</label>
                        <input
                            type="date"
                            name="dateOfBirth"
                            value={formData.dateOfBirth}
                            onChange={handleChange}
                            placeholder="dd/mm/yyyy"
                        />
                    </div>
                    <div className="form-group">
                        <label>Allergies:</label>
                        <input
                            type="text"
                            name="allergies"
                            value={formData.allergies}
                            onChange={handleChange}
                            placeholder="Enter known allergies"
                        />
                    </div>
                </div>
                <div className="form-row">
                    <div className="form-group">
                        <label>Phone Number:</label>
                        <input
                            type="tel"
                            name="phoneNumber"
                            value={formData.phoneNumber}
                            onChange={handleChange}
                            placeholder="Enter phone number"
                        />
                    </div>
                    <div className="form-group">
                        <label>Address:</label>
                        <textarea
                            name="address"
                            value={formData.address}
                            onChange={handleChange}
                            placeholder="Enter address"
                        />
                    </div>
                </div>
                <div className="form-row">
                    <div className="form-group">
                        <label>Emergency Contact Number:</label>
                        <input
                            type="tel"
                            name="emergencyContact"
                            value={formData.emergencyContact}
                            onChange={handleChange}
                            placeholder="Enter emergency contact number"
                        />
                    </div>
                </div>
                <div className="form-actions">
                    <button type="submit" className="invite-button">Invite</button>
                    <button
                        type="button"
                        className="back-invite-button"
                        onClick={() => navigate('/institution-dashboard')}
                    >
                        Back
                    </button>
                </div>
                {successMessage && <p className="success-message">{successMessage}</p>}
                {errorMessage && <p className="error-message">{errorMessage}</p>}

                <footer className="invite-user-footer">
                    &copy; Copyright {new Date().getFullYear()} | VaidyaLink
                </footer>
            </form>
        </div>
    );
};

export default InviteUser;
