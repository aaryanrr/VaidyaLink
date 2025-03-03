package com.mustard.vaidyalink.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blockchain")
public class BlockchainController {

    @GetMapping("/health")
    public String health() {
        return "Healthy";
    }
}
