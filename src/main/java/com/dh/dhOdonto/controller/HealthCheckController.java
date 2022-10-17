package com.dh.dhOdonto.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HealthCheckController {

    @GetMapping(path = "/check")
    public ResponseEntity buscar() {
        return new ResponseEntity("OK", HttpStatus.OK);
    }
}
