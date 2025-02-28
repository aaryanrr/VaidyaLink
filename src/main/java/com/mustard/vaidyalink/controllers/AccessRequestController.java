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

import java.util.UUID;
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

    @GetMapping("/approve")
    public ResponseEntity<?> approveAccessRequest(@RequestParam UUID id) {
        try {
            accessRequestService.approveAccessRequest(id);
            return ResponseEntity.ok("Access request approved successfully");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
