package com.mustard.vaidyalink.services;

import com.mustard.vaidyalink.contract.MedicalDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

@Service
public class BlockchainService {

    private final MedicalDataAccess contract;

    @Autowired
    public BlockchainService(Web3j web3j, Credentials credentials) {
        try {
            ContractGasProvider gasProvider = new DefaultGasProvider();
            this.contract = MedicalDataAccess.deploy(web3j, credentials, gasProvider).send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void logAccessRequestCreated(String aadhaarHash, String details) {
        try {
            contract.logAccessRequestCreated(aadhaarHash, details).send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void logAccessRequestApproved(String aadhaarHash, String details) {
        try {
            contract.logAccessRequestApproved(aadhaarHash, details).send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void logAccessRevoked(String aadhaarHash, String details) {
        try {
            contract.logAccessRevoked(aadhaarHash, details).send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void logUserInvited(String aadhaarHash, String details) {
        try {
            contract.logUserInvited(aadhaarHash, details).send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void logInstitutionRegistered(String registrationNumber, String details) {
        try {
            contract.logInstitutionRegistered(registrationNumber, details).send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void logDataEditedForUser(String aadhaarHash, String details) {
        try {
            contract.logDataEditedForUser(aadhaarHash, details).send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void logBasicDataViewed(String aadhaarHash, String details) {
        try {
            contract.logBasicDataViewed(aadhaarHash, details).send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void logUserAccountDeleted(String aadhaarHash, String details) {
        try {
            contract.logUserAccountDeleted(aadhaarHash, details).send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void logUserViewedRecords(String aadhaarHash, String details) {
        try {
            contract.logUserViewedRecords(aadhaarHash, details).send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
