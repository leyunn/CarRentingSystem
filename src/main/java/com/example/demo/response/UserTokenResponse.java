package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Utility Class for returning the token info of a user
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserTokenResponse {
    private Long id;
    private String token;

    private boolean isRenter;

}
