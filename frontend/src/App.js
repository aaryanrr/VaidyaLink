import React from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Home from './components/Home';
import Institution from './components/InstitutionLogin';
import Users from './components/UserLogin';
import About from './components/About';
import SignUp from './components/InstitutionSignUp';
import InstitutionSignUp from './components/InstitutionSignUp';
import NotFound from './components/NotFound';
import InstitutionDashboard from "./components/InstitutionDashboard";

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
                    <Route path="/institution-dashboard" element={<InstitutionDashboard/>}/>
                    <Route path="*" element={<NotFound/>}/>
                </Routes>
            </div>
        </Router>
    );
}

export default App;
