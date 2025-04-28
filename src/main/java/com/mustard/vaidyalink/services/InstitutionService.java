package com.mustard.vaidyalink.services;

import com.mustard.vaidyalink.entities.Institution;
import com.mustard.vaidyalink.entities.Token;
import com.mustard.vaidyalink.repositories.InstitutionRepository;
import com.mustard.vaidyalink.repositories.TokenRepository;
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
import java.util.Optional;

@Service
public class InstitutionService {

    private final InstitutionRepository institutionRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(InstitutionService.class);


    public InstitutionService(InstitutionRepository institutionRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository, JwtUtil jwtUtil) {
        this.institutionRepository = institutionRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.jwtUtil = jwtUtil;
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
}
