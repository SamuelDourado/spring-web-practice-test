package com.example.demo.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/debug")
public class UserAgentInspectorController {
    
    @GetMapping("/user-agent")
    public ResponseEntity<String> checkUserAgent(HttpServletRequest request) {
        String agent = request.getHeader("User-Agent");
        int status = (agent == null || agent.isBlank()) ? HttpStatus.BAD_REQUEST.value() : HttpStatus.OK.value();
        return ResponseEntity
            .status(status)
            .header(HttpHeaders.CACHE_CONTROL, "no-store")
            .body("User-Agent: " + request.getHeader("User-Agent"));
    }
}
