package com.mustard.vaidyalink.services;

import com.mustard.vaidyalink.dtos.AccessRequestDto;
import com.mustard.vaidyalink.entities.AccessRequest;
import com.mustard.vaidyalink.repositories.AccessRequestRepository;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class AccessRequestService {

    private final AccessRequestRepository accessRequestRepository;

    public AccessRequestService(AccessRequestRepository accessRequestRepository) {
        this.accessRequestRepository = accessRequestRepository;
    }

    public void createAccessRequest(AccessRequestDto requestDto) {
        String hashedAadhaar = hashAadhaar(requestDto.getAadhaarNumber());

        AccessRequest accessRequest = new AccessRequest();
        accessRequest.setId(UUID.randomUUID());
        accessRequest.setAadhaarNumber(hashedAadhaar);
        accessRequest.setDataCategory(requestDto.getDataCategory());
        accessRequest.setTimePeriod(requestDto.getTimePeriod());
        accessRequest.setActionRequired(requestDto.getActionRequired());
        accessRequest.setApproved(false);

        accessRequestRepository.save(accessRequest);
    }

    private String hashAadhaar(String aadhaar) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(aadhaar.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing Aadhaar number", e);
        }
    }
}
