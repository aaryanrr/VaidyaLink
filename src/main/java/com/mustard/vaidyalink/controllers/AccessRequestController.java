package com.mustard.vaidyalink.controllers;

import com.mustard.vaidyalink.dtos.AccessRequestDto;
import com.mustard.vaidyalink.entities.Institution;
import com.mustard.vaidyalink.services.InstitutionService;
import com.mustard.vaidyalink.services.AccessRequestService;
import com.mustard.vaidyalink.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/access-requests")
public class AccessRequestController {

    private final AccessRequestService accessRequestService;
    private final InstitutionService institutionService;
    private final JwtUtil jwtUtil;

    public AccessRequestController(AccessRequestService accessRequestService, InstitutionService institutionService, JwtUtil jwtUtil) {
        this.accessRequestService = accessRequestService;
        this.institutionService = institutionService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/request")
    public ResponseEntity<String> createAccessRequest(@RequestBody AccessRequestDto requestDto, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7).trim();
            String institutionEmail = jwtUtil.extractUsername(token);

            Optional<Institution> institutionOptional = institutionService.findByEmail(institutionEmail);
            if (institutionOptional.isPresent()) {
                Institution institution = institutionOptional.get();
                accessRequestService.createAccessRequest(requestDto, institution.getInstitutionName(), institution.getRegistrationNumber());
                return ResponseEntity.ok("Access request submitted successfully");
            } else {
                return ResponseEntity.status(401).body("Unauthorized");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
        }
    }

    @PostMapping("/approve-access")
    public ResponseEntity<?> approveAccessRequestWithPassword(@RequestBody Map<String, String> body) {
        String encodedId = body.get("accessRequestId");
        String encodedPassword = body.get("password");
        if (encodedId == null || encodedPassword == null) {
            return ResponseEntity.badRequest().body("Missing fields.");
        }
        String accessRequestId;
        String password;
        try {
            accessRequestId = new String(java.util.Base64.getDecoder().decode(encodedId));
            password = new String(java.util.Base64.getDecoder().decode(encodedPassword));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid encoding.");
        }
        try {
            accessRequestService.approveAccessRequestWithPassword(accessRequestId, password);
            return ResponseEntity.ok("Access Request Approved Successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
