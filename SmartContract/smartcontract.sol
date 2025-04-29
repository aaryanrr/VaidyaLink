pragma solidity ^0.8.0;

contract EventLogger {
    event AccessRequestCreated(string aadhaarHash, string details);
    event AccessRequestApproved(string aadhaarHash, string details);
    event AccessRevoked(string aadhaarHash, string details);
    event UserInvited(string aadhaarHash, string details);
    event InstitutionRegistered(string registrationNumber, string details);
    event DataEditedForUser(string aadhaarHash, string details);
    event BasicDataViewed(string aadhaarHash, string details);
    event UserAccountDeleted(string aadhaarHash, string details);
    event UserViewedRecords(string aadhaarHash, string details);

    function logAccessRequestCreated(string calldata aadhaarHash, string calldata details) external {
        emit AccessRequestCreated(aadhaarHash, details);
    }

    function logAccessRequestApproved(string calldata aadhaarHash, string calldata details) external {
        emit AccessRequestApproved(aadhaarHash, details);
    }

    function logAccessRevoked(string calldata aadhaarHash, string calldata details) external {
        emit AccessRevoked(aadhaarHash, details);
    }

    function logUserInvited(string calldata aadhaarHash, string calldata details) external {
        emit UserInvited(aadhaarHash, details);
    }

    function logInstitutionRegistered(string calldata registrationNumber, string calldata details) external {
        emit InstitutionRegistered(registrationNumber, details);
    }

    function logDataEditedForUser(string calldata aadhaarHash, string calldata details) external {
        emit DataEditedForUser(aadhaarHash, details);
    }

    function logBasicDataViewed(string calldata aadhaarHash, string calldata details) external {
        emit BasicDataViewed(aadhaarHash, details);
    }

    function logUserAccountDeleted(string calldata aadhaarHash, string calldata details) external {
        emit UserAccountDeleted(aadhaarHash, details);
    }

    function logUserViewedRecords(string calldata aadhaarHash, string calldata details) external {
        emit UserViewedRecords(aadhaarHash, details);
    }
}
