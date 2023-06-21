/**
 * The TestController class is a simple controller used for testing purposes.
 * It provides an endpoint for testing the functionality of the application.
 */
package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    /**
     * Handles the test request and returns a success message.
     *
     * @return ResponseEntity containing the success message.
     */
    @GetMapping("/")
    public String test() {
        return "success";
    }
}
