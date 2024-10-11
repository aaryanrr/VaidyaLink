package com.mustard.vaidyalink.services;

import com.mustard.vaidyalink.entities.User;
import com.mustard.vaidyalink.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
