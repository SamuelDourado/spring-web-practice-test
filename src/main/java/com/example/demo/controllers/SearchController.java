package com.example.demo.controllers;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {
    
    @GetMapping("/items")
    public ResponseEntity<String> search(
        @RequestParam(name = "page", defaultValue = "0") int page, 
        @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Boolean isPageValid = page >= 0;
        Boolean isSizeValid = size > 0 && size <= 100;
        if (!isPageValid || !isSizeValid) {
            return ResponseEntity.badRequest().body("Invalid pagination parameters");
        }
        
        String result = String.format("page=%d, size=%d", page, size);
        return ResponseEntity.ok(result);
    }
}
