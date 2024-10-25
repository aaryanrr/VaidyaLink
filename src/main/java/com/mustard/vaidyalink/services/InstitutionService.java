package com.mustard.vaidyalink.services;

import com.mustard.vaidyalink.entities.Institution;
import com.mustard.vaidyalink.entities.Token;
import com.mustard.vaidyalink.repositories.InstitutionRepository;
import com.mustard.vaidyalink.repositories.TokenRepository;
import com.mustard.vaidyalink.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class InstitutionService {

    private final InstitutionRepository institutionRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public InstitutionService(InstitutionRepository institutionRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository, JwtUtil jwtUtil) {
        this.institutionRepository = institutionRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.jwtUtil = jwtUtil;
    }

    public void registerInstitution(String email, String rawPassword, MultipartFile licenseFile) {
        if (institutionRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Institution with this email already exists.");
        }

        String licenseFilePath = saveFile(licenseFile);

        Institution institution = new Institution();
        institution.setEmail(email);
        institution.setPassword(passwordEncoder.encode(rawPassword));
        institution.setLicenseFilePath(licenseFilePath);
        institution.setRegistrationNumber(generateRegistrationNumber());

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

    public String saveFile(MultipartFile file) {
        try {
            String uploadDir = "uploads/";

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);

            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error while saving file", e);
        }
    }

    private String generateRegistrationNumber() {
        Random random = new Random();
        int randomNum = 100000 + random.nextInt(900000);
        return "IN" + randomNum;
    }

    public void saveToken(String token, Institution institution) {
        Token jwtToken = new Token();
        jwtToken.setToken(token);
        jwtToken.setExpiryDate(LocalDateTime.now().plusHours(10));
        jwtToken.setInstitution(institution);
        tokenRepository.save(jwtToken);
    }

    public boolean validateToken(String token) {
        Optional<Token> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isPresent()) {
            Token jwtToken = tokenOptional.get();
            return !jwtToken.getExpiryDate().isBefore(LocalDateTime.now());
        }
        return false;
    }

    public void invalidateToken(String token) {
        tokenRepository.deleteByToken(token);
    }
}
