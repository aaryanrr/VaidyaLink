package com.mustard.vaidyalink.services;

import com.mustard.vaidyalink.contract.MedicalDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class BlockchainService {

    private static final String CONTRACT_ADDRESS_PATH = "contract_address.txt";
    private final MedicalDataAccess contract;

    @Autowired
    public BlockchainService(Web3j web3j, Credentials credentials, Environment env) {
        try {
            ContractGasProvider gasProvider = new DefaultGasProvider();
            String contractAddress = loadContractAddress();
            MedicalDataAccess loadedContract = null;
            if (contractAddress != null) {
                loadedContract = MedicalDataAccess.load(contractAddress, web3j, credentials, gasProvider);
                if (!loadedContract.isValid()) {
                    loadedContract = null;
                }
            }
            if (loadedContract == null) {
                MedicalDataAccess deployed = MedicalDataAccess.deploy(web3j, credentials, gasProvider).send();
                contractAddress = deployed.getContractAddress();
                saveContractAddress(contractAddress);
                this.contract = deployed;
            } else {
                this.contract = loadedContract;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String loadContractAddress() {
        try {
            File file = new File(CONTRACT_ADDRESS_PATH);
            if (file.exists()) {
                return new String(Files.readAllBytes(Paths.get(CONTRACT_ADDRESS_PATH))).trim();
            }
        } catch (IOException ignored) {
        }
        return null;
    }

    private void saveContractAddress(String address) {
        try {
            Files.write(Paths.get(CONTRACT_ADDRESS_PATH), address.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save contract address", e);
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
