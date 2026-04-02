package com.redearth.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HomeControler {
    
    @GetMapping("/")
    public String home() {
        return "Welcome to Red Earth Beadworks!";
    }
}
