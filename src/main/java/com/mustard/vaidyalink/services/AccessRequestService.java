package com.mustard.vaidyalink.services;

import com.mustard.vaidyalink.dtos.AccessRequestDto;
import com.mustard.vaidyalink.entities.AccessRequest;
import com.mustard.vaidyalink.repositories.AccessRequestRepository;
import com.mustard.vaidyalink.entities.User;
import com.mustard.vaidyalink.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.Optional;

@Service
public class AccessRequestService {

    private final AccessRequestRepository accessRequestRepository;
    private final UserRepository userRepository;
    private final MailgunService mailgunService;

    public AccessRequestService(AccessRequestRepository accessRequestRepository, MailgunService mailgunService, UserRepository userRepository) {
        this.accessRequestRepository = accessRequestRepository;
        this.mailgunService = mailgunService;
        this.userRepository = userRepository;
    }

    public void createAccessRequest(AccessRequestDto requestDto, String institutionName, String institutionRegNum) {
        String hashedAadhaar = hashAadhaar(requestDto.getAadhaarNumber());
        Optional<User> userOptional = userRepository.findByAadhaarNumberHash(hashedAadhaar);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            AccessRequest accessRequest = new AccessRequest();
            accessRequest.setAadhaarNumber(hashedAadhaar);
            accessRequest.setDataCategory(String.join(", ", requestDto.getDataCategory()));
            accessRequest.setTimePeriod(requestDto.getTimePeriod());
            accessRequest.setActionRequired(String.join(", ", requestDto.getActionRequired()));
            accessRequest.setApproved(false);
            // FIXME: ObjectOptimisticLockingFailureException is thrown when saving the entity
            accessRequestRepository.save(accessRequest);

            generateAccessEmail(accessRequest, institutionName, institutionRegNum, requestDto, user);

        } else {
            throw new IllegalArgumentException("User with provided Aadhaar Number not Found!");
        }
    }

    public void approveAccessRequest(UUID id) {
        Optional<AccessRequest> accessRequestOptional = accessRequestRepository.findById(id);
        if (accessRequestOptional.isPresent()) {
            AccessRequest accessRequest = accessRequestOptional.get();
            accessRequest.setApproved(true);
            accessRequestRepository.save(accessRequest);
        } else {
            throw new IllegalArgumentException("Access Request with provided ID not Found!");
        }
    }

    private void generateAccessEmail(AccessRequest accessRequest, String institutionName, String institutionRegNum, AccessRequestDto requestDto, User user) {
        String emailBody = String.format(
                """
                        Dear %s,
                        
                        An access request has been made by %s (Reg. No: %s) with the following details:
                        
                        Data Category: %s
                        Time Period: %s
                        Action Required: %s
                        
                        Please click the following link to approve or deny this request:
                        http://localhost:8080/api/access-requests/approve?id=%s
                        
                        Sincerely,
                        VaidyaLink Team""",
                user.getName(),
                institutionName,
                institutionRegNum,
                String.join(", ", requestDto.getDataCategory()),
                requestDto.getTimePeriod(),
                String.join(", ", requestDto.getActionRequired()),
                accessRequest.getId()
        );
        mailgunService.sendEmail(user.getEmail(), "VaidyaLink Access Request Notification", emailBody);

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
