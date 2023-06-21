package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Utility Class for return an error json response with an error message
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorResponse {
    private String error;
}
