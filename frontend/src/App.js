import React from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Home from './components/Home';
import Institution from './components/institution/InstitutionLogin';
import Users from './components/user/UserLogin';
import About from './components/About';
import SignUp from './components/institution/InstitutionSignUp';
import InstitutionSignUp from './components/institution/InstitutionSignUp';
import NotFound from './components/NotFound';
import InstitutionDashboard from './components/institution/InstitutionDashboard';
import UserDashboard from './components/user/UserDashboard';
import PrivateRoute from './components/PrivateRoute';
import InviteUser from './components/institution/InviteUser';
import AccessRequest from './components/institution/AccessRequest';
import ViewRecords from './components/user/ViewRecords';
import AccessHistory from './components/user/AccessHistory';
import DeleteAccount from './components/user/DeleteAccount';
import RevokeAccess from './components/user/RevokeAccess';
import AccessHistoryInstitution from "./components/institution/AccessHistoryInstitution";

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
                    <Route
                        path="/view-records"
                        element={
                            <PrivateRoute>
                                <ViewRecords/>
                            </PrivateRoute>
                        }
                    />
                    <Route
                        path="/access-history"
                        element={
                            <PrivateRoute>
                                <AccessHistory/>
                            </PrivateRoute>
                        }
                    />
                    <Route
                        path="/delete-account"
                        element={
                            <PrivateRoute>
                                <DeleteAccount/>
                            </PrivateRoute>
                        }/>
                    <Route
                        path="/revoke-access"
                        element={
                            <PrivateRoute>
                                <RevokeAccess/>
                            </PrivateRoute>
                        }/>
                    <Route
                        path="/access-history-ins"
                        element={
                            <PrivateRoute>
                                <AccessHistoryInstitution/>
                            </PrivateRoute>
                        }/>
                    <Route path="*" element={<NotFound/>}/>
                </Routes>
            </div>
        </Router>
    );
}

export default App;
