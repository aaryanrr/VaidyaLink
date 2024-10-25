package com.mustard.vaidyalink.controllers;

import com.mustard.vaidyalink.entities.User;
import com.mustard.vaidyalink.services.MyUserDetailsService;
import com.mustard.vaidyalink.services.UserService;
import com.mustard.vaidyalink.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public UserController(AuthenticationManager authenticationManager, MyUserDetailsService userDetailsService, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam String aadhaar, @RequestParam String password) {
        String aadhaarHash = hashAadhaar(aadhaar);
        userService.registerUser(aadhaarHash, password);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/invite")
    public ResponseEntity<String> inviteUser(@RequestParam String name, @RequestParam String email,
                                             @RequestParam String aadhaar, @RequestParam String phoneNumber,
                                             @RequestParam String dateOfBirth, @RequestParam String address,
                                             @RequestParam String bloodGroup, @RequestParam String emergencyContact,
                                             @RequestParam String allergies, @RequestParam Double heightCm,
                                             @RequestParam Double weightKg) {

        String aadhaarHash = hashAadhaar(aadhaar);
        LocalDate dob = LocalDate.parse(dateOfBirth); // Assumes date is provided in 'yyyy-MM-dd' format
        userService.inviteUser(name, email, aadhaarHash, phoneNumber, dob, address, bloodGroup, emergencyContact, allergies, heightCm, weightKg);
        return ResponseEntity.ok("User invited successfully!");
    }

    @GetMapping("/find")
    public ResponseEntity<User> findUser(@RequestParam String aadhaar) {
        String aadhaarHash = hashAadhaar(aadhaar);
        Optional<User> user = userService.findByAadhaarNumberHash(aadhaarHash);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getAadhaarNumberHash(), user.getPassword())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid Aadhaar number or password.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getAadhaarNumberHash());
        String jwt = jwtUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(jwt);
    }

    private String hashAadhaar(String aadhaar) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(aadhaar.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while hashing Aadhaar number", e);
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
