package com.mustard.vaidyalink.controllers;

import com.mustard.vaidyalink.entities.AccessRequest;
import com.mustard.vaidyalink.entities.Institution;
import com.mustard.vaidyalink.services.InstitutionService;
import com.mustard.vaidyalink.utils.JwtUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<?> registerInstitution(
            @RequestParam String institutionName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam("licenseFile") MultipartFile licenseFile) {
        try {
            institutionService.registerInstitution(institutionName, email, password, licenseFile);
            return ResponseEntity.ok("Institution registered successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during registration.");
        }
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

    @PostMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        boolean isValid = institutionService.validateToken(jwtToken);
        System.out.println("Token is valid: " + isValid + " " + token);
        return isValid ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }

    @GetMapping("/current-access")
    public ResponseEntity<?> getCurrentAccess(@RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String email = jwtUtil.extractUsername(jwtToken);
        Optional<Institution> institutionOpt = institutionService.findByEmail(email);
        if (institutionOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }
        Institution institution = institutionOpt.get();
        String regNum = institution.getRegistrationNumber();
        List<AccessRequest> requests = institutionService.getApprovedAccessRequests(regNum);
        List<Map<String, Object>> result = requests.stream().map(req -> {
            Map<String, Object> map = new HashMap<>();
            map.put("userEmail", institutionService.getUserEmailByAadhaarHash(req.getAadhaarNumber()));
            map.put("requestId", req.getId().toString());
            map.put("approved", req.getApproved());
            map.put("timePeriod", req.getTimePeriod().toString());
            map.put("basicDataLink", "http://localhost:3000/basic-data?id=" + req.getId());
            map.put("medicalReportsLink", "http://localhost:3000/medical-reports?id=" + req.getId());
            map.put("actionRequested", req.getActionRequired());
            return map;
        }).toList();

        return ResponseEntity.ok(result);
    }

    @PostMapping("/basic-data")
    public ResponseEntity<?> getOrEditBasicData(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> body
    ) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String email = jwtUtil.extractUsername(jwtToken);
        Optional<Institution> institutionOpt = institutionService.findByEmail(email);
        if (institutionOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }
        String accessRequestId = (String) body.get("accessRequestId");
        String accessKey = (String) body.get("accessKey");
        boolean isEdit = "true".equals(body.getOrDefault("edit", "false"));
        Map<String, String> editData = null;
        if (isEdit && body.get("editData") instanceof Map) {
            ObjectMapper mapper = new ObjectMapper();
            editData = mapper.convertValue(body.get("editData"), new TypeReference<>() {
            });
        }
        try {
            Map<String, Object> response = institutionService.getOrEditBasicDataWithAccessRights(accessRequestId, accessKey, isEdit, editData);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
