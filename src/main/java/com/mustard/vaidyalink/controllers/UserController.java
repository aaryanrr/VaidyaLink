package com.mustard.vaidyalink.controllers;

import com.mustard.vaidyalink.dtos.InviteRequest;
import com.mustard.vaidyalink.entities.User;
import com.mustard.vaidyalink.services.UserService;
import com.mustard.vaidyalink.utils.JwtUtil;
import com.mustard.vaidyalink.utils.EncryptionUtil;
import com.mustard.vaidyalink.entities.AccessRequest;
import com.mustard.vaidyalink.services.AccessRequestService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AccessRequestService accessRequestService;

    public UserController(JwtUtil jwtUtil, UserService userService, AccessRequestService accessRequestService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.accessRequestService = accessRequestService;
    }

    @PostMapping("/invite")
    public ResponseEntity<String> inviteUser(@RequestBody InviteRequest inviteRequest) {
        String aadhaarHash = hashAadhaar(inviteRequest.getAadhaar());
        LocalDate dob = LocalDate.parse(inviteRequest.getDateOfBirth());

        userService.inviteUser(
                inviteRequest.getName(),
                inviteRequest.getEmail(),
                aadhaarHash,
                inviteRequest.getPhoneNumber(),
                dob,
                inviteRequest.getAddress(),
                inviteRequest.getBloodGroup(),
                inviteRequest.getEmergencyContact(),
                inviteRequest.getAllergies(),
                inviteRequest.getHeightCm(),
                inviteRequest.getWeightKg()
        );

        return ResponseEntity.ok("User invited successfully!");
    }

    @GetMapping("/find")
    public ResponseEntity<User> findUser(@RequestParam String aadhaar) {
        String aadhaarHash = hashAadhaar(aadhaar);
        Optional<User> user = userService.findByAadhaarNumberHash(aadhaarHash);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestUser loginRequestUser) {
        boolean success = userService.login(loginRequestUser.getEmail(), loginRequestUser.getPassword());
        if (success) {
            String token = jwtUtil.generateToken(loginRequestUser.getEmail());
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }
    }

    @GetMapping("/records")
    public ResponseEntity<?> getUserRecords(@RequestHeader("Authorization") String token,
                                            @RequestHeader("X-Password") String encodedPassword) {
        token = token.replace("Bearer ", "").trim();
        String email = jwtUtil.extractUsername(token);
        Optional<User> user = userService.findByEmail(email);

        if (user.isPresent()) {
            String password = new String(java.util.Base64.getDecoder().decode(encodedPassword));
            Map<String, Object> userRecords = Map.of(
                    "email", user.get().getEmail(),
                    "phoneNumber", EncryptionUtil.encryptDecryptString("decrypt", user.get().getPhoneNumber(), password),
                    "dateOfBirth", EncryptionUtil.encryptDecryptDate("decrypt", user.get().getDateOfBirth(), password),
                    "address", EncryptionUtil.encryptDecryptString("decrypt", user.get().getAddress(), password),
                    "bloodGroup", EncryptionUtil.encryptDecryptString("decrypt", user.get().getBloodGroup(), password),
                    "emergencyContact", EncryptionUtil.encryptDecryptString("decrypt", user.get().getEmergencyContact(), password),
                    "allergies", EncryptionUtil.encryptDecryptString("decrypt", user.get().getAllergies(), password),
                    "heightCm", EncryptionUtil.encryptDecryptDouble("decrypt", user.get().getHeightCm(), password),
                    "weightKg", EncryptionUtil.encryptDecryptDouble("decrypt", user.get().getWeightKg(), password)
            );
            return ResponseEntity.ok(userRecords);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }
    }

    @GetMapping("/access-requests")
    public ResponseEntity<?> getUserAccessRequests(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "").trim();
        String email = jwtUtil.extractUsername(token);

        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }
        User user = userOpt.get();
        String aadhaarHash = user.getAadhaarNumberHash();

        List<AccessRequest> requests = accessRequestService.getAccessRequestsByAadhaarHash(aadhaarHash);

        List<Map<String, String>> result = requests.stream().map(req -> Map.of(
                "id", req.getId().toString(),
                "institutionName", req.getInstitutionName(),
                "actionRequested", req.getActionRequired(),
                "timePeriod", req.getTimePeriod().toString(),
                "approved", req.getApproved() != null && req.getApproved() ? "Yes" : "No",
                "requestedAt", req.getRequestedAt().toString()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "").trim();
        boolean isValid = userService.isTokenValid(token);
        return isValid ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        boolean isRemoved = userService.invalidateToken(jwtToken);

        if (isRemoved) {
            return ResponseEntity.ok("Logged out successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found or already invalidated.");
        }
    }

    @PostMapping("/delete-account")
    public ResponseEntity<String> deleteAccount(@RequestHeader("Authorization") String token,
                                                @RequestBody Map<String, String> body) {
        token = token.replace("Bearer ", "").trim();
        String email = jwtUtil.extractUsername(token);
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }
        User user = userOpt.get();
        String encodedPassword = body.get("password");
        String password = new String(java.util.Base64.getDecoder().decode(encodedPassword));
        if (!userService.isPasswordCorrect(user, password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect Password");
        }
        userService.deleteUserAndTokens(user, token);
        return ResponseEntity.ok("Account deleted successfully.");

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

    @Setter
    @Getter
    public static class LoginRequestUser {
        private String email;
        private String password;
    }
}
