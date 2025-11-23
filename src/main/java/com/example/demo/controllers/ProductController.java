package com.example.demo.controllers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProductDTO;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final Map<Long, ProductDTO> productDB = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO product) {
        ProductDTO newProduct = new ProductDTO(
            idGenerator.getAndIncrement(),
            product.name(),
            product.price()
        );

        productDB.put(newProduct.id(), newProduct);
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(newProduct);
    }
}
