// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract MedicalDataAccess {
    
    struct Patient {
        string name;
        string email;
        string aadhaarNumber;
        string phone;
        string dob;
        string addressDetail;
        string bloodGroup;
        string emergencyContact;
        string allergies;
        uint256 heightCm;
        uint256 weightKg;
        // Mapping for authorized institutions
        mapping(address => AccessDetails) authorizedInstitutions;
    }
    
    struct AccessDetails {
        bool canRead;
        bool canWrite;
        uint256 expiryDate;
        uint256 grantedOn;
        bool accessRevoked;
    }

    struct Request {
        address institution;
        string aadhaarNumber;
        string dataCategory; // Basic Data or Medical Reports
        string department; // Only for Medical Reports
        string actionRequired; // Read or Write
        uint256 requestedTill;
        bool isApproved;
    }

    mapping(address => Patient) public patients;
    mapping(address => Request[]) public requestsByInstitution;

    event AccessRequested(address indexed institution, string aadhaarNumber, string dataCategory, uint256 requestedTill);
    event AccessGranted(address indexed institution, string aadhaarNumber, uint256 timestamp);
    event AccessRevoked(address indexed institution, string aadhaarNumber, uint256 timestamp);

    modifier onlyPatient(string memory aadhaarNumber) {
        require(
            keccak256(abi.encodePacked(patients[msg.sender].aadhaarNumber)) == keccak256(abi.encodePacked(aadhaarNumber)),
            "Not authorized: Only the patient can perform this action."
        );
        _;
    }

    // Register a patient with their basic data
    function registerPatient(
        string memory _name, 
        string memory _email, 
        string memory _aadhaarNumber,
        string memory _phone, 
        string memory _dob, 
        string memory _addressDetail, 
        string memory _bloodGroup, 
        string memory _emergencyContact, 
        string memory _allergies, 
        uint256 _heightCm, 
        uint256 _weightKg
    ) public {
        // Ensure patient is not already registered
        require(bytes(patients[msg.sender].aadhaarNumber).length == 0, "Patient is already registered.");
        
        // Initialize the patient struct fields individually (due to the nested mapping)
        patients[msg.sender].name = _name;
        patients[msg.sender].email = _email;
        patients[msg.sender].aadhaarNumber = _aadhaarNumber;
        patients[msg.sender].phone = _phone;
        patients[msg.sender].dob = _dob;
        patients[msg.sender].addressDetail = _addressDetail;
        patients[msg.sender].bloodGroup = _bloodGroup;
        patients[msg.sender].emergencyContact = _emergencyContact;
        patients[msg.sender].allergies = _allergies;
        patients[msg.sender].heightCm = _heightCm;
        patients[msg.sender].weightKg = _weightKg;
    }

    // Institutions request access to patient data
    function requestAccess(
        string memory _aadhaarNumber, 
        string memory _dataCategory, 
        string memory _department, 
        string memory _actionRequired, 
        uint256 _requestedTill
    ) public {
        requestsByInstitution[msg.sender].push(
            Request({
                institution: msg.sender,
                aadhaarNumber: _aadhaarNumber,
                dataCategory: _dataCategory,
                department: _department,
                actionRequired: _actionRequired,
                requestedTill: _requestedTill,
                isApproved: false
            })
        );
        emit AccessRequested(msg.sender, _aadhaarNumber, _dataCategory, _requestedTill);
    }

    // Patient grants access to an institution
    function grantAccess(
        address _institution, 
        string memory _aadhaarNumber, 
        uint256 _expiryDate, 
        bool _canRead, 
        bool _canWrite
    ) public onlyPatient(_aadhaarNumber) {
        patients[msg.sender].authorizedInstitutions[_institution] = AccessDetails({
            canRead: _canRead,
            canWrite: _canWrite,
            expiryDate: _expiryDate,
            grantedOn: block.timestamp,
            accessRevoked: false
        });
        
        emit AccessGranted(_institution, _aadhaarNumber, block.timestamp);
    }

    // Patient revokes access to an institution
    function revokeAccess(address _institution, string memory _aadhaarNumber) public onlyPatient(_aadhaarNumber) {
        require(patients[msg.sender].authorizedInstitutions[_institution].canRead || patients[msg.sender].authorizedInstitutions[_institution].canWrite, "No active access to revoke.");
        
        patients[msg.sender].authorizedInstitutions[_institution].canRead = false;
        patients[msg.sender].authorizedInstitutions[_institution].canWrite = false;
        patients[msg.sender].authorizedInstitutions[_institution].accessRevoked = true;
        
        emit AccessRevoked(_institution, _aadhaarNumber, block.timestamp);
    }

    // Check if an institution has access to specific patient data
    function checkAccess(address _institution, string memory _aadhaarNumber) public view returns (bool canRead, bool canWrite, uint256 expiryDate, bool accessRevoked) {
        AccessDetails memory accessDetails = patients[msg.sender].authorizedInstitutions[_institution];
        return (accessDetails.canRead, accessDetails.canWrite, accessDetails.expiryDate, accessDetails.accessRevoked);
    }
}

