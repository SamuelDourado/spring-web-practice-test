package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/upload")
public class UploadController {
    
    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file" ) MultipartFile file) {
        long fileSize = file.getSize();
        String fileName = file.getOriginalFilename();

        return ResponseEntity.ok(String.format("filename=%s, size=%d", fileName, fileSize));
    }

    @GetMapping
    public String getMethodName() {
        return "olá";
    }
    
}
