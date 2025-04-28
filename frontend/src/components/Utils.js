import {useNavigate} from 'react-router-dom';

export function UserRedirectToHome() {
    const navigate = useNavigate();
    return () => navigate('/');
}

export function UserRedirectToUserDashboard() {
    const navigate = useNavigate();
    return () => navigate("/user-dashboard");
}

export function UserRedirectToInstitutionDashboard() {
    const navigate = useNavigate();
    return () => navigate("/institution-dashboard");
}