package com.spring.blog.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/healthCheck")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
