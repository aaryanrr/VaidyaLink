package com.mustard.vaidyalink.controllers;

import com.mustard.vaidyalink.dtos.AccessRequestDto;
import com.mustard.vaidyalink.services.AccessRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/access-requests")
public class AccessRequestController {

    private final AccessRequestService accessRequestService;

    public AccessRequestController(AccessRequestService accessRequestService) {
        this.accessRequestService = accessRequestService;
    }

    @PostMapping("/request")
    public ResponseEntity<String> createAccessRequest(@RequestBody AccessRequestDto requestDto) {
        accessRequestService.createAccessRequest(requestDto);
        return ResponseEntity.ok("Access request submitted successfully");
    }
}
