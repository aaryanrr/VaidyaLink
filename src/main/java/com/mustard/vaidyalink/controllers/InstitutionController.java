package com.mustard.vaidyalink.controllers;

import com.mustard.vaidyalink.entities.Institution;
import com.mustard.vaidyalink.services.InstitutionService;
import com.mustard.vaidyalink.utils.JwtUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {

    private final InstitutionService institutionService;
    private final JwtUtil jwtUtil;

    public InstitutionController(InstitutionService institutionService, JwtUtil jwtUtil) {
        this.institutionService = institutionService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerInstitution(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam("licenseFile") MultipartFile licenseFile) {
        institutionService.registerInstitution(email, password, licenseFile);
        return ResponseEntity.ok("Institution registered successfully!");
    }

    @GetMapping("/find")
    public ResponseEntity<Institution> findInstitution(@RequestParam String email) {
        Optional<Institution> institution = institutionService.findByEmail(email);
        return institution.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        boolean success = institutionService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (success) {
            String token = jwtUtil.generateToken(loginRequest.getEmail());
            return ResponseEntity.ok().body(new LoginResponse(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard(@RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String email = jwtUtil.extractUsername(jwtToken);
        Optional<Institution> institution = institutionService.findByEmail(email);

        if (institution.isPresent() && institutionService.validateToken(jwtToken)) {
            return ResponseEntity.ok("Access granted to dashboard");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied. Please login.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        boolean isRemoved = institutionService.invalidateToken(jwtToken);

        if (isRemoved) {
            return ResponseEntity.ok("Logged out successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found or already invalidated.");
        }
    }

    @Setter
    @Getter
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Setter
    @Getter
    public static class LoginResponse {
        private String token;

        public LoginResponse(String token) {
            this.token = token;
        }
    }
}
