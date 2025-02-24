package com.mustard.vaidyalink.services;

import com.mustard.vaidyalink.entities.Token;
import com.mustard.vaidyalink.entities.User;
import com.mustard.vaidyalink.repositories.UserRepository;
import com.mustard.vaidyalink.repositories.TokenRepository;
import com.mustard.vaidyalink.utils.JwtUtil;
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
        user.setName(name);
        user.setEmail(email);
        user.setAadhaarNumberHash(aadhaarNumberHash);
        user.setPhoneNumber(phoneNumber);
        user.setDateOfBirth(dateOfBirth);
        user.setAddress(address);
        user.setBloodGroup(bloodGroup);
        user.setEmergencyContact(emergencyContact);
        user.setAllergies(allergies);
        user.setHeightCm(heightCm);
        user.setWeightKg(weightKg);
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

    public void registerUser(String aadhaarNumberHash, String rawPassword) {
        User user = new User();
        user.setAadhaarNumberHash(aadhaarNumberHash);
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
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
        Optional<Token> tokenEntity = tokenRepository.findByToken(token);
        return tokenEntity.isPresent() && tokenEntity.get().getExpiryDate().isAfter(LocalDate.now().atStartOfDay());
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
