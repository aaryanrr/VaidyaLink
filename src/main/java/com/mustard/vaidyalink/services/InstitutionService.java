package com.mustard.vaidyalink.services;

import com.mustard.vaidyalink.entities.*;
import com.mustard.vaidyalink.repositories.InstitutionRepository;
import com.mustard.vaidyalink.repositories.TokenRepository;
import com.mustard.vaidyalink.repositories.InstitutionAccessDataRepository;
import com.mustard.vaidyalink.repositories.AccessRequestRepository;
import com.mustard.vaidyalink.repositories.UserRepository;
import com.mustard.vaidyalink.utils.EncryptionUtil;
import com.mustard.vaidyalink.utils.JwtUtil;
import com.mustard.vaidyalink.utils.GenerationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class InstitutionService {

    private final InstitutionRepository institutionRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final InstitutionAccessDataRepository institutionAccessDataRepository;
    private final AccessRequestRepository accessRequestRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(InstitutionService.class);


    public InstitutionService(InstitutionRepository institutionRepository, PasswordEncoder passwordEncoder,
                              TokenRepository tokenRepository, JwtUtil jwtUtil,
                              InstitutionAccessDataRepository institutionAccessDataRepository,
                              AccessRequestRepository accessRequestRepository, UserRepository userRepository) {
        this.institutionRepository = institutionRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.jwtUtil = jwtUtil;
        this.institutionAccessDataRepository = institutionAccessDataRepository;
        this.accessRequestRepository = accessRequestRepository;
        this.userRepository = userRepository;
    }

    public void registerInstitution(String institutionName, String email, String rawPassword, MultipartFile licenseFile) {
        if (institutionRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Institution with this email already exists.");
        }

        String licenseFilePath = saveFile(licenseFile, rawPassword);

        Institution institution = new Institution();
        institution.setInstitutionName(institutionName);
        institution.setEmail(email);
        institution.setPassword(passwordEncoder.encode(rawPassword));
        institution.setLicenseFilePath(licenseFilePath);
        institution.setRegistrationNumber(generateAndCheckRegNumber());

        institutionRepository.save(institution);
    }

    public Optional<Institution> findByEmail(String email) {
        return institutionRepository.findByEmail(email);
    }

    public boolean login(String email, String password) {
        Optional<Institution> institutionOptional = institutionRepository.findByEmail(email);
        if (institutionOptional.isPresent()) {
            Institution institution = institutionOptional.get();
            if (passwordEncoder.matches(password, institution.getPassword())) {
                String token = jwtUtil.generateToken(institution.getEmail());
                saveToken(token, institution);
                return true;
            }
        }
        return false;
    }

    public String saveFile(MultipartFile file, String password) {
        try {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("No file uploaded.");
            }
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".pdf")) {
                throw new IllegalArgumentException("Only PDF files are allowed.");
            }
            byte[] magic = new byte[4];
            file.getInputStream().read(magic, 0, 4);
            if (!(magic[0] == 0x25 && magic[1] == 0x50 && magic[2] == 0x44 && magic[3] == 0x46)) {
                throw new IllegalArgumentException("Uploaded file is not a valid PDF.");
            }
            String uploadDir = "uploads";
            Path uploadPath = Paths.get(uploadDir).normalize().toAbsolutePath();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String safeFileName = java.util.UUID.randomUUID() + ".pdf";
            Path tempFilePath = uploadPath.resolve("temp_" + safeFileName);
            Path encryptedFilePath = uploadPath.resolve(safeFileName);
            Files.copy(file.getInputStream(), tempFilePath);
            EncryptionUtil.encryptDecryptFile("encrypt", tempFilePath.toFile(), encryptedFilePath.toFile(), password);
            Files.delete(tempFilePath);
            return encryptedFilePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error while saving file", e);
        }
    }


    private String generateAndCheckRegNumber() {
        String regNum;
        do {
            regNum = GenerationUtil.generateRegistrationNumber();
        } while (institutionRepository.findByRegistrationNumber(regNum).isPresent());
        return regNum;
    }


    public void saveToken(String token, Institution institution) {
        Token jwtToken = new Token();
        jwtToken.setToken(token);
        jwtToken.setExpiryDate(LocalDateTime.now().plusHours(10));
        jwtToken.setInstitution(institution);
        tokenRepository.save(jwtToken);
    }

    public boolean validateToken(String token) {
        return tokenRepository.existsByTokenAndInstitutionIsNotNullAndUserIsNull(token);
    }

    public boolean invalidateToken(String token) {
        Optional<Token> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isPresent()) {
            tokenRepository.delete(tokenOptional.get());
            logger.info("Token deleted successfully: {}", token);
            return true;
        } else {
            logger.warn("Token not found for deletion: {}", token);
            return false;
        }
    }

    public List<AccessRequest> getApprovedAccessRequests(String regNum) {
        return accessRequestRepository.findAllByInstitutionRegistrationNumberAndApprovedIsTrue(regNum);
    }

    public String getUserEmailByAadhaarHash(String aadhaarHash) {
        return userRepository.findByAadhaarNumberHash(aadhaarHash)
                .map(User::getEmail)
                .orElse("N/A");
    }

    public Map<String, Object> getOrEditBasicDataWithAccessRights(String accessRequestId, String accessKey, boolean isEdit, Map<String, String> editData) {
        UUID reqId = UUID.fromString(accessRequestId);
        AccessRequest req = accessRequestRepository.findById(reqId)
                .orElseThrow(() -> new IllegalArgumentException("Access request not found."));
        String actionRequested = req.getActionRequired();
        boolean canEdit = actionRequested != null && actionRequested.contains("Write");
        boolean canView = actionRequested != null && (actionRequested.contains("Read") || actionRequested.contains("Edit"));

        if (!req.getApproved()) throw new IllegalArgumentException("Access Request Not approved");

        InstitutionAccessData data = institutionAccessDataRepository.findAllByAccessRequestId(reqId)
                .stream().findFirst().orElseThrow(() -> new IllegalArgumentException("No data found for this access request."));

        if (isEdit) {
            if (!canEdit) throw new IllegalArgumentException("You do not have write access.");
            data.setPhoneNumber(EncryptionUtil.encryptDecryptString("encrypt", editData.get("phoneNumber"), accessKey));
            data.setAddress(EncryptionUtil.encryptDecryptString("encrypt", editData.get("address"), accessKey));
            data.setBloodGroup(EncryptionUtil.encryptDecryptString("encrypt", editData.get("bloodGroup"), accessKey));
            data.setEmergencyContact(EncryptionUtil.encryptDecryptString("encrypt", editData.get("emergencyContact"), accessKey));
            data.setAllergies(EncryptionUtil.encryptDecryptString("encrypt", editData.get("allergies"), accessKey));
            data.setHeightCm(EncryptionUtil.encryptDecryptDouble("encrypt", Double.valueOf(editData.get("heightCm")), accessKey));
            data.setWeightKg(EncryptionUtil.encryptDecryptDouble("encrypt", Double.valueOf(editData.get("weightKg")), accessKey));
            institutionAccessDataRepository.save(data);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("name", EncryptionUtil.encryptDecryptString("decrypt", data.getName(), accessKey));
        result.put("email", EncryptionUtil.encryptDecryptString("decrypt", data.getEmail(), accessKey));
        result.put("phoneNumber", EncryptionUtil.encryptDecryptString("decrypt", data.getPhoneNumber(), accessKey));
        result.put("dateOfBirth", EncryptionUtil.encryptDecryptDate("decrypt", data.getDateOfBirth(), accessKey));
        result.put("address", EncryptionUtil.encryptDecryptString("decrypt", data.getAddress(), accessKey));
        result.put("bloodGroup", EncryptionUtil.encryptDecryptString("decrypt", data.getBloodGroup(), accessKey));
        result.put("emergencyContact", EncryptionUtil.encryptDecryptString("decrypt", data.getEmergencyContact(), accessKey));
        result.put("allergies", EncryptionUtil.encryptDecryptString("decrypt", data.getAllergies(), accessKey));
        result.put("heightCm", EncryptionUtil.encryptDecryptDouble("decrypt", data.getHeightCm(), accessKey));
        result.put("weightKg", EncryptionUtil.encryptDecryptDouble("decrypt", data.getWeightKg(), accessKey));
        result.put("canEdit", canEdit);
        result.put("canView", canView);
        return result;
    }


}
