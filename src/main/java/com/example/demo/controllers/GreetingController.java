package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GreetingController {
    
    @GetMapping("/hello/{name}")
    public String sayHello(@RequestParam(value="lang", defaultValue="en") String lang, @PathVariable(name="name") String name) {
        String message;
        switch (lang) {
            case "es":
                message = "¡Hola," + name + "!";
                break;
            case "pt":
                message = "Olá," + name + "!";
                break;
            default:
                message = "Hello, " + name + "!";
        }
        return message;
    }
}