package com.mustard.vaidyalink;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/api/status")
    public String status() {
        return "API is running { version : 1.0.0 }";
    }
}
