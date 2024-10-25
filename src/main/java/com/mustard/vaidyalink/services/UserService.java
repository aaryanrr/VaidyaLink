package com.mustard.vaidyalink.services;

import com.mustard.vaidyalink.entities.User;
import com.mustard.vaidyalink.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.security.SecureRandom;

import com.mustard.vaidyalink.services.MailgunService;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailgunService mailgunService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MailgunService mailgunService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailgunService = mailgunService;
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

        userRepository.save(user);
        mailgunService.sendPasswordEmail(email, "Your Temporary Password", rawPassword);
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
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";
        StringBuilder password = new StringBuilder(12);

        for (int i = 0; i < 12; i++) {
            int randomIndex = secureRandom.nextInt(allowedChars.length());
            password.append(allowedChars.charAt(randomIndex));
        }
        return password.toString();
    }
}
