import {useNavigate} from 'react-router-dom';

export function UserRedirectToHome() {
    const navigate = useNavigate();
    return () => navigate('/');
}