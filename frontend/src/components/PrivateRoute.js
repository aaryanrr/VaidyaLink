import React from 'react';
import {Navigate, useLocation} from 'react-router-dom';

const PrivateRoute = ({children}) => {
    const token = localStorage.getItem('token');
    const location = useLocation();

    if (!token) {
        if (location.pathname.includes('institution-dashboard') || location.pathname.includes('invite-new-user')) {
            return <Navigate to="/institution" replace/>;
        }
        return <Navigate to="/users" replace/>;
    }

    return children;
};

export default PrivateRoute;
