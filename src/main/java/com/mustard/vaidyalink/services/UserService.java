package com.mustard.vaidyalink.services;

import com.mustard.vaidyalink.entities.Token;
import com.mustard.vaidyalink.entities.User;
import com.mustard.vaidyalink.repositories.UserRepository;
import com.mustard.vaidyalink.repositories.TokenRepository;
import com.mustard.vaidyalink.utils.JwtUtil;
import com.mustard.vaidyalink.utils.EncryptionUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.security.SecureRandom;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailgunService mailgunService;
    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MailgunService mailgunService, TokenRepository tokenRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailgunService = mailgunService;
        this.tokenRepository = tokenRepository;
        this.jwtUtil = jwtUtil;
    }

    public void inviteUser(String name, String email, String aadhaarNumberHash, String phoneNumber, LocalDate dateOfBirth,
                           String address, String bloodGroup, String emergencyContact, String allergies, Double heightCm, Double weightKg) {
        String rawPassword = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = new User();
        user.setName(EncryptionUtil.encryptDecryptString("encrypt", name, rawPassword));
        user.setEmail(email);
        user.setAadhaarNumberHash(aadhaarNumberHash);
        user.setPhoneNumber(EncryptionUtil.encryptDecryptString("encrypt", phoneNumber, rawPassword));
        user.setDateOfBirth(EncryptionUtil.encryptDecryptDate("encrypt", dateOfBirth, rawPassword));
        user.setAddress(EncryptionUtil.encryptDecryptString("encrypt", address, rawPassword));
        user.setBloodGroup(EncryptionUtil.encryptDecryptString("encrypt", bloodGroup, rawPassword));
        user.setEmergencyContact(EncryptionUtil.encryptDecryptString("encrypt", emergencyContact, rawPassword));
        user.setAllergies(EncryptionUtil.encryptDecryptString("encrypt", allergies, rawPassword));
        user.setHeightCm(EncryptionUtil.encryptDecryptDouble("encrypt", heightCm, rawPassword));
        user.setWeightKg(EncryptionUtil.encryptDecryptDouble("encrypt", weightKg, rawPassword));
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);
        if (savedUser.getId() != null) {
            mailgunService.sendPasswordEmail(email, "VaidyaLink Login Password", rawPassword);
        } else {
            throw new RuntimeException("Failed to save user to the database.");
        }
    }

    public boolean login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtUtil.generateToken(user.getEmail());
                saveToken(token, user);
                return true;
            }
        }
        return false;
    }

    public Optional<User> findByAadhaarNumberHash(String aadhaarNumberHash) {
        return userRepository.findByAadhaarNumberHash(aadhaarNumberHash);
    }

    private String generateRandomPassword() {
        SecureRandom secureRandom = new SecureRandom();
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*-_+=";
        StringBuilder password = new StringBuilder(12);

        for (int i = 0; i < 12; i++) {
            int randomIndex = secureRandom.nextInt(allowedChars.length());
            password.append(allowedChars.charAt(randomIndex));
        }
        return password.toString();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveToken(String token, User user) {
        Token jwtToken = new Token();
        jwtToken.setToken(token);
        jwtToken.setExpiryDate(LocalDateTime.now().plusHours(10));
        jwtToken.setUser(user);
        tokenRepository.save(jwtToken);
    }

    public boolean isTokenValid(String token) {
        return tokenRepository.existsByTokenAndUserIsNotNullAndInstitutionIsNull(token);
    }

    public boolean invalidateToken(String token) {
        Optional<Token> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isPresent()) {
            tokenRepository.delete(tokenOptional.get());
            return true;
        } else {
            return false;
        }
    }

}
