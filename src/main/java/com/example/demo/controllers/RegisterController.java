package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.requests.RegistrationRequest;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/register")
public class RegisterController {
    
    @PostMapping()
    public ResponseEntity<String> register(@RequestBody @Valid RegistrationRequest request, BindingResult result) {
        if(result.hasErrors()) {
            String errors = result.getAllErrors().toString();
            return ResponseEntity.badRequest().body(errors);

        }
        
        return ResponseEntity.ok("Registered: " + request.getEmail());
    }
}
