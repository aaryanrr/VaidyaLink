package com.mustard.vaidyalink.controllers;

import com.mustard.vaidyalink.services.SyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sync")
public class SyncController {

    private final SyncService syncService;

    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }

    @PostMapping("/sync-approved")
    public ResponseEntity<?> syncApproved(@RequestBody Map<String, String> body) {
        String encodedAccessRequestId = body.get("accessRequestId");
        String encodedAccessKey = body.get("accessKey");
        String encodedPassword = body.get("password");
        if (encodedAccessRequestId == null || encodedAccessKey == null || encodedPassword == null) {
            return ResponseEntity.badRequest().body("Missing fields.");
        }
        try {
            syncService.syncApproved(encodedAccessRequestId, encodedAccessKey, encodedPassword);
            return ResponseEntity.ok("Data sync complete.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
