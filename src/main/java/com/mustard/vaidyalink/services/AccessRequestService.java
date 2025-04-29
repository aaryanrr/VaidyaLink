package com.mustard.vaidyalink.services;

import com.mustard.vaidyalink.dtos.AccessRequestDto;
import com.mustard.vaidyalink.entities.AccessRequest;
import com.mustard.vaidyalink.entities.InstitutionAccessData;
import com.mustard.vaidyalink.entities.Institution;
import com.mustard.vaidyalink.entities.User;
import com.mustard.vaidyalink.repositories.InstitutionAccessDataRepository;
import com.mustard.vaidyalink.repositories.InstitutionRepository;
import com.mustard.vaidyalink.repositories.AccessRequestRepository;
import com.mustard.vaidyalink.repositories.UserRepository;
import com.mustard.vaidyalink.utils.EncryptionUtil;
import com.mustard.vaidyalink.utils.GenerationUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.UUID;
import java.util.Optional;
import java.util.List;

@Service
public class AccessRequestService {

    private final AccessRequestRepository accessRequestRepository;
    private final UserRepository userRepository;
    private final MailgunService mailgunService;
    private final InstitutionAccessDataRepository institutionAccessDataRepository;
    private final InstitutionRepository institutionRepository;
    private final PasswordEncoder passwordEncoder;
    private final BlockchainService blockchainService;

    public AccessRequestService(AccessRequestRepository accessRequestRepository, MailgunService mailgunService,
                                UserRepository userRepository, InstitutionAccessDataRepository institutionAccessDataRepository,
                                InstitutionRepository institutionRepository, PasswordEncoder passwordEncoder,
                                BlockchainService blockchainService) {
        this.accessRequestRepository = accessRequestRepository;
        this.mailgunService = mailgunService;
        this.userRepository = userRepository;
        this.institutionAccessDataRepository = institutionAccessDataRepository;
        this.institutionRepository = institutionRepository;
        this.passwordEncoder = passwordEncoder;
        this.blockchainService = blockchainService;
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
            accessRequest.setInstitutionName(institutionName);
            accessRequest.setInstitutionRegistrationNumber(institutionRegNum);
            accessRequestRepository.save(accessRequest);

            generateAccessEmail(accessRequest, institutionName, institutionRegNum, requestDto, user);
            blockchainService.logAccessRequestCreated(user.getEmail(), "Access request created by Institution " + institutionRegNum);
        } else {
            throw new IllegalArgumentException("User with provided Aadhaar Number not Found!");
        }
    }

    public List<AccessRequest> getAccessRequestsByAadhaarHash(String aadhaarHash) {
        return accessRequestRepository.findAllByAadhaarNumber(aadhaarHash);
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
                        http://localhost:3000/approve-access-request?id=%s
                        
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

    public boolean revokeAccessRequest(String accessRequestId, String userAadhaarHash) {
        try {
            UUID uuid = UUID.fromString(accessRequestId);
            Optional<AccessRequest> reqOpt = accessRequestRepository.findById(uuid);
            if (reqOpt.isEmpty()) return false;
            AccessRequest req = reqOpt.get();
            if (!req.getAadhaarNumber().equals(userAadhaarHash) || !Boolean.TRUE.equals(req.getApproved())) {
                return false;
            }
            req.setApproved(false);
            accessRequestRepository.save(req);
            institutionAccessDataRepository.deleteAll(institutionAccessDataRepository.findAllByAccessRequestId(uuid));
            blockchainService.logAccessRevoked(req.getAadhaarNumber(), "Access request revoked for Institution " + req.getInstitutionRegistrationNumber());
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public List<AccessRequest> getAccessRequestsByInstitutionRegistrationNumber(String regNum) {
        return accessRequestRepository.findAllByInstitutionRegistrationNumber(regNum);
    }

    public void approveAccessRequestWithPassword(String accessRequestId, String password) {
        UUID uuid = UUID.fromString(accessRequestId);
        Optional<AccessRequest> accessRequestOpt = accessRequestRepository.findById(uuid);
        if (accessRequestOpt.isEmpty()) throw new IllegalArgumentException("Invalid access request ID.");
        AccessRequest accessRequest = accessRequestOpt.get();
        String aadhaarHash = accessRequest.getAadhaarNumber();
        Optional<User> userOpt = userRepository.findByAadhaarNumberHash(aadhaarHash);
        if (userOpt.isEmpty()) throw new IllegalArgumentException("User not found.");
        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new IllegalArgumentException("Incorrect password.");
        accessRequest.setApproved(true);
        accessRequestRepository.save(accessRequest);

        String institutionRegNum = accessRequest.getInstitutionRegistrationNumber();

        String newAccessPassword = GenerationUtil.generateSecurePassword();

        InstitutionAccessData data = new InstitutionAccessData();
        data.setAccessRequestId(accessRequest.getId());
        data.setInstitutionRegistrationNumber(institutionRegNum);
        data.setName(EncryptionUtil.encryptDecryptString("encrypt", EncryptionUtil.encryptDecryptString("decrypt", user.getName(), password), newAccessPassword));
        data.setEmail(EncryptionUtil.encryptDecryptString("encrypt", user.getEmail(), newAccessPassword));
        data.setPhoneNumber(EncryptionUtil.encryptDecryptString("encrypt", EncryptionUtil.encryptDecryptString("decrypt", user.getPhoneNumber(), password), newAccessPassword));
        data.setDateOfBirth(EncryptionUtil.encryptDecryptDate("encrypt", EncryptionUtil.encryptDecryptDate("decrypt", user.getDateOfBirth(), password), newAccessPassword));
        data.setAddress(EncryptionUtil.encryptDecryptString("encrypt", EncryptionUtil.encryptDecryptString("decrypt", user.getAddress(), password), newAccessPassword));
        data.setBloodGroup(EncryptionUtil.encryptDecryptString("encrypt", EncryptionUtil.encryptDecryptString("decrypt", user.getBloodGroup(), password), newAccessPassword));
        data.setEmergencyContact(EncryptionUtil.encryptDecryptString("encrypt", EncryptionUtil.encryptDecryptString("decrypt", user.getEmergencyContact(), password), newAccessPassword));
        data.setAllergies(EncryptionUtil.encryptDecryptString("encrypt", EncryptionUtil.encryptDecryptString("decrypt", user.getAllergies(), password), newAccessPassword));
        data.setHeightCm(EncryptionUtil.encryptDecryptDouble("encrypt", EncryptionUtil.encryptDecryptDouble("decrypt", user.getHeightCm(), password), newAccessPassword));
        data.setWeightKg(EncryptionUtil.encryptDecryptDouble("encrypt", EncryptionUtil.encryptDecryptDouble("decrypt", user.getWeightKg(), password), newAccessPassword));
        institutionAccessDataRepository.save(data);

        Optional<Institution> instOpt = institutionRepository.findByRegistrationNumber(institutionRegNum);
        instOpt.ifPresent(inst -> mailgunService.sendEmail(inst.getEmail(),
                "VaidyaLink Access Key",
                "Your access key for request ID " + accessRequestId + " is: " + newAccessPassword));
        blockchainService.logAccessRequestApproved(user.getEmail(), "Access request approved for Institution " + institutionRegNum);
    }

    @Scheduled(fixedRate = 3600000)
    public void revokeExpiredAccessRequests() {
        List<AccessRequest> expired = accessRequestRepository.findAllByTimePeriodBeforeAndApprovedIsTrue(LocalDate.now());
        for (AccessRequest req : expired) {
            req.setApproved(false);
            accessRequestRepository.save(req);
            institutionAccessDataRepository.deleteAll(institutionAccessDataRepository.findAllByAccessRequestId(req.getId()));
            Optional<User> userOpt = userRepository.findByAadhaarNumberHash(req.getAadhaarNumber());
            Optional<Institution> instOpt = institutionRepository.findByRegistrationNumber(req.getInstitutionRegistrationNumber());
            userOpt.ifPresent(user -> blockchainService.logAccessRevoked(
                    user.getEmail(), "Expired access requests revoked for Institution " + req.getInstitutionRegistrationNumber()));
            userOpt.ifPresent(user -> mailgunService.sendEmail(user.getEmail(),
                    "VaidyaLink Access Revoked",
                    "Your access for request ID " + req.getId() + " has been revoked due to expiry."));
            instOpt.ifPresent(institution -> mailgunService.sendEmail(institution.getEmail(),
                    "VaidyaLink Access Revoked",
                    "Your access for request ID " + req.getId() + " has been revoked due to expiry."));
        }
    }

}
