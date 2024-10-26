import React from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Home from './components/Home';
import Institution from './components/InstitutionLogin';
import Users from './components/UserLogin';
import About from './components/About';
import SignUp from './components/InstitutionSignUp';
import InstitutionSignUp from './components/InstitutionSignUp';
import NotFound from './components/NotFound';
import InstitutionDashboard from './components/InstitutionDashboard';
import UserDashboard from './components/UserDashboard';
import PrivateRoute from './components/PrivateRoute';
import InviteUser from './components/InviteUser';
import AccessRequest from './components/AccessRequest';

function App() {
    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/" element={<Home/>}/>
                    <Route path="/institution" element={<Institution/>}/>
                    <Route path="/users" element={<Users/>}/>
                    <Route path="/about" element={<About/>}/>
                    <Route path="/signup" element={<SignUp/>}/>
                    <Route path="/institution-signup" element={<InstitutionSignUp/>}/>
                    <Route
                        path="/institution-dashboard"
                        element={
                            <PrivateRoute>
                                <InstitutionDashboard/>
                            </PrivateRoute>
                        }
                    />
                    <Route
                        path="/user-dashboard"
                        element={
                            <PrivateRoute>
                                <UserDashboard/>
                            </PrivateRoute>
                        }
                    />
                    <Route
                        path="/invite-new-user"
                        element={
                            <PrivateRoute>
                                <InviteUser/>
                            </PrivateRoute>
                        }
                    />
                    <Route
                        path="/access-request"
                        element={
                            <PrivateRoute>
                                <AccessRequest/>
                            </PrivateRoute>
                        }
                    />
                    <Route path="*" element={<NotFound/>}/>
                </Routes>
            </div>
        </Router>
    );
}

export default App;
