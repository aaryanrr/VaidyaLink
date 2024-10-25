package com.mustard.vaidyalink.services;

import com.mustard.vaidyalink.entities.User;
import com.mustard.vaidyalink.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void inviteUser(String name, String email, String aadhaarNumberHash, String phoneNumber, LocalDate dateOfBirth,
                           String address, String bloodGroup, String emergencyContact, String allergies, Double heightCm, Double weightKg) {
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
        userRepository.save(user);
    }

    // Existing methods remain the same
    public void registerUser(String aadhaarNumberHash, String rawPassword) {
        User user = new User();
        user.setAadhaarNumberHash(aadhaarNumberHash);
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
    }

    public Optional<User> findByAadhaarNumberHash(String aadhaarNumberHash) {
        return userRepository.findByAadhaarNumberHash(aadhaarNumberHash);
    }
}
