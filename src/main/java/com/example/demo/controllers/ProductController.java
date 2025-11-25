package com.example.demo.controllers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProductDTO;
import com.example.demo.exceptions.ResourceNotFoundException;

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

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable int id) {
        ProductDTO product = productDB.get((long) id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            throw new ResourceNotFoundException("Resource not found ");
        }
    }
}
